require 'spec_helper'
require 'ap'
require 'db_models'

describe 'pong watcher UI' do
    context 'match schedule' do
        before do
            match1 = Match.new(
                id: 1001,
                name: 'Bruce',
                phone: '2025551212',
                created: Time.now,
                updated: Time.now
            )
            match1.save()

            match2 = Match.new(
                id: 1002,
                name: 'Izzy',
                phone: '7205551212',
                created: 20.minutes.ago,
                updated: 15.minutes.ago
            )
            match2.save()
        end

        it 'displays the schedule of matches' do
             visit '/matches'

             expect(page).to have_selector '#match-table'
             expect(page).to have_css('.match-row', count: 2)
        end

        it 'displays a link to a form to create a new match' do
            visit '/matches'
            expect(page).to have_selector(:link_or_button, 'New Match')

            click_link_or_button 'New Match'

            fill_in 'name', :with => 'Elaina'
            fill_in 'phone', :with => '3035551212'
            fill_in 'location', :with => 'Upstairs'

            click_link_or_button 'Save'

            expect(page).to have_selector '#match-table'
        end
    end
end