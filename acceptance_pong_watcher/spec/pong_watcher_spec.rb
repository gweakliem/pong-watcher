require 'spec_helper'
require 'ap'
require 'db_models'

describe 'pong watcher' do
    before do
        @es = EventSimulator.new
        @es.run
        sleep 1
    end

    context 'activity monitor API' do
        it 'supplies a history of motion events' do
            history_resp = HTTParty.get("http://localhost:9000/api/activity/history")
            expect(history_resp.code).to eq 200

            history = JSON.parse(history_resp.body, :symbolize_names => true)

            expect(history[:motionEvents]).to_not be_nil
            expected_events = @es.events.map { |e|
                { eventTime: e[:ts].chomp(":00"), motionDetected: e[:m] }
            }

            expect(history[:motionEvents].first).to eq(expected_events.first)
        end

        it 'supplies the last motion event' do
            history_resp = HTTParty.get("http://localhost:9000/api/activity/lastevent")
            expect(history_resp.code).to eq 200

            history = JSON.parse(history_resp.body, :symbolize_names => true)

            expect(history[:eventTime]).to_not be_nil
            expect(history[:motionDetected]).to be true
            latest_event = @es.events.find { |e| e[:m] == true }
            expect(history[:eventTime]).to eq latest_event[:ts]
        end
    end

    context 'match schedule API' do
        before do
            match1 = Match.new(
                id: 1,
                name: 'Bruce',
                phone: '2025551212',
                created: Time.now,
                updated: Time.now
            )
            match1.save()

            match2 = Match.new(
                id: 2,
                name: 'Izzy',
                phone: '7205551212',
                created: 20.minutes.ago,
                updated: 15.minutes.ago
            )
            match2.save()
        end

        it 'allows the caller to retrieve a match entity' do
            resp = HTTParty.get("http://localhost:9000/api/matches/1")
            expect(resp.code).to eq 200
        end

        it 'allows a player to schedule a match' do
            resp = HTTParty.post('http://localhost:9000/api/matches',
                {
                 headers: { 'Content-Type' => 'application/json'},
                 body: { name: 'Player 1'}.to_json
                })

            expect(resp.code).to eq 201
            expect(resp.headers['location']).to_not be_nil

            match = JSON.parse(resp.body, :symbolize_names => true)
            expect(match[:name]).to eq 'Player 1'
            expect(match[:completed]).to be_nil
            expect(match[:created]).to_not be_nil
            expect(match[:updated]).to_not be_nil
            expect(match[:id]).to_not be_nil
        end

        it 'displays the schedule of matches' do
            resp = HTTParty.get("http://localhost:9000/api/matches")

            expect(resp.code).to eq 200

            matches = JSON.parse(resp.body, :symbolize_names => true)
            expect(matches[0][:name]).to eq 'Bruce'
            expect(matches[1][:name]).to eq 'Izzy'

        end

        context 'when a match is scheduled' do
            before do
                resp = HTTParty.post("http://localhost:9000/api/matches",
                            {
                             headers: { 'Content-Type' => 'application/json'},
                             body: { name: 'Ready Player 1'}.to_json
                            })
                @match_url = resp.headers['location']
            end

            it 'allows a player to record a match as played' do
                match = JSON.parse(HTTParty.get("http://localhost:9000#{@match_url}").body)
                match['completed'] = '2016-11-24T11:56:56.450'
                resp = HTTParty.put('http://localhost:9000' + @match_url,
                    {
                        headers: { 'Content-Type' => 'application/json'},
                        body: match.to_json
                    })
                expect(resp.code).to eq 200
            end

            it 'allows a player to cancel a match' do
                 resp = HTTParty.delete("http://localhost:9000#{@match_url}")
                 expect(resp.code).to eq 204
            end
        end
    end
end