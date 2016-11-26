db_dir = File.expand_path('../..', __FILE__)
support = File.expand_path('../support', __FILE__)
$LOAD_PATH.unshift(support) unless $LOAD_PATH.include?(support)

require 'colorize'
require 'httparty'
require 'active_record'

#require 'rspec'
require 'json_matchers/rspec'
require 'mqtt'
require 'time'

require 'database_cleaner'
DatabaseCleaner.strategy = :truncation

class EventSimulator
    attr_reader :events
    def run
        @events = []
        10.times { |i|
            t = Time.now - (10 * i)
            @events.push({ts: t.strftime("%FT%T"),
                             m: [true, false].sample})
        }

        MQTT::Client.connect('mqtt://localhost:1883') do |c|
            @events.each { |e|
              c.publish('motion_sensor/events', e.to_json)
            }
        end
    end
end

class Mosquitto
    def run
        puts "Starting mosquitto"

        @pid = Process.spawn("mosquitto",
            chdir: File.expand_path('../../../',__FILE__),
            pgroup: true,
            out: 'mosquitto.std.log',
            err: 'mosquitto.err.log')

        puts "Started mosquitto at pid #{@pid}"

        at_exit {
                    puts "Stopping processs #{@pid}"
                    Process.kill('TERM',@pid)
                }
    end
end

class H2
    def run
        puts "Starting h2"

        @pid = Process.spawn("h2 -pg -pgPort 5432",
            chdir: File.expand_path('../../../',__FILE__),
            pgroup: true,
            out: 'h2.std.log',
            err: 'h2.err.log')

        puts "Started h2 at pid #{@pid}"

        at_exit {
                    puts "Stopping h2 processs #{@pid}"
                    Process.kill('TERM',@pid)
                }
    end
end

class PongWatcher
    attr_accessor :port

    def run
        if health_is_ok? "http://localhost:#{@port}/health"
            puts 'app already up'.yellow
        else
            puts "Starting app"

            @pid = Process.spawn("SERVER_PORT=#{@port} ./gradlew bootRun",
                chdir: File.expand_path('../../../',__FILE__),
                pgroup: true,
                out: 'pong.std.log',
                err: 'pong.err.log')

            puts "Started app at pid #{@pid}"

            wait_for_app_to_start

            puts "App launched"

            at_exit {
                puts "Stopping processs #{@pid}"
                Process.kill('TERM',@pid)
            }
        end
    end

    def wait_for_app_to_start
        attempts = 0
        until health_is_ok? "http://localhost:#{@port}/health"
            attempts = attempts + 1
            if attempts >= 60
                raise "app never came up after #{attempts} seconds"
            end

            sleep 1
            print "\r[#{attempts} / 60] waiting for app to start".yellow
        end
    end

    def health_is_ok?(url)
        begin
            HTTParty.get(url).code == 200
        rescue Errno::ECONNREFUSED
            false
        end
    end
end

RSpec.configure do |config|
    config.filter_run :focus
    config.run_all_when_everything_filtered = true

    config.default_formatter = 'doc'
    config.order = :random
    Kernel.srand config.seed

    config.before :suite do
        DatabaseCleaner.clean
        puts 'seeding event publisher'
    end

    config.before :each do
        DatabaseCleaner.clean
    end

    ActiveRecord::Base.establish_connection(
        adapter: 'postgresql',
        database: 'pong_watch',
        host: 'localhost',
        username: 'ping',
        password: 'pong',
        schema_search_path: 'pong_watch'
    )

    #h2 = H2.new
    #h2.run

    mq = Mosquitto.new
    mq.run

    app = PongWatcher.new
    app.port = 9000
    app.run

end
