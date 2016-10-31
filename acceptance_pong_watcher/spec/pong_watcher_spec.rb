require 'spec_helper'
require 'ap'

describe 'pong watcher' do
    before do
    end

    it 'supplies a history of motion events' do
        history_resp = HTTParty.get("http://localhost:9000/api/history")
        expect(history_resp.code).to eq 200

        history = JSON.parse(history_resp.body, :symbolize_names => true)

        expect(history[:motionEvents]).to_not be_nil
    end

    it 'supplies the last motion event' do
        history_resp = HTTParty.get("http://localhost:9000/api/lastevent")
        expect(history_resp.code).to eq 200

        history = JSON.parse(history_resp.body, :symbolize_names => true)

        expect(history[:eventTime]).to_not be_nil
        expect(history[:motionDetected]).to be true
    end
end