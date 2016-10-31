support = File.expand_path('../support', __FILE__)
$LOAD_PATH.unshift(support) unless $LOAD_PATH.include?(support)

require 'colorize'
require 'httparty'

#require 'rspec'
require 'json_matchers/rspec'
require 'mqtt'
require 'time'

#require 'database_cleaner'

#DatabaseCleaner.strategy = :truncation

class EventSimulator
    def run
        rng = Random.new

        MQTT::Client.connect('mqtt://localhost:1883') do |c|
        10.times {
        puts 'publishing'
          c.publish('motion_sensor/events',
                {ts: Time.new.strftime("%FT%T%:z"),
                    m: rng.rand(0..1) ? true:false}.to_json)
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

class PongWatcher
    attr_accessor :port

    def run
        if health_is_ok? "http://localhost:#{@port}/health"
            puts 'app already up'.yellow
        else
            puts "Starting app"

            puts File.expand_path('../../../',__FILE__)

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
        until health_is_ok? "http://localhost:#{port}/health"
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
            puts "Checking #{url}"
            HTTParty.get(url).code == 200
        rescue Errno::ECONNREFUSED
            false
        end
    end
end

RSpec.configure do |config|
    #config.filter_run :focus

    config.default_formatter = 'doc'
    config.order = :random
    Kernel.srand config.seed

    config.before :suite do
        #DatabaseCleaner.clean
    end

    mq = Mosquitto.new
    mq.run

    app = PongWatcher.new
    app.port = 9000
    app.run

    es = EventSimulator.new
    es.run

end
