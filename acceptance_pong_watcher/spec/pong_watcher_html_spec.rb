require 'spec_helper'
require 'ap'
require 'db_models'

describe 'pong watcher' do
    context 'match UI' do
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

        it 'displays links to update and cancel a match' do
            visit '/matches'

            expect(page).to have_selector(:link_or_button, 'Edit', count: 2)
            expect(page).to have_selector(:link_or_button, 'Cancel', count: 2)
        end

        it 'provides a 404 page' do
            visit '/matches/editor/99999999'

            expect(page).to have_title "Not Found"
        end

        context 'editing operations' do
            it 'provides a way to create a new match' do
                visit '/matches'
                expect(page).to have_selector(:link_or_button, 'New Match')

                click_link_or_button 'New Match'

                fill_in 'name', :with => 'Elaina'
                fill_in 'phone', :with => '3035551212'
                fill_in 'location', :with => 'Upstairs'

                click_link_or_button 'Save'

                expect(page).to have_selector '#match-table'
                expect(page).to have_css('.match-row', count: 3)
            end

            it 'provides a way to edit a match' do
                visit '/matches'

                page.find('.match-row', :match => :first).click_link('Edit')

                fill_in 'name', :with => 'Addy'
                fill_in 'phone', :with => '6095551212'
                fill_in 'location', :with => 'Downstairs'

                click_link_or_button 'Save'

                expect(page).to have_selector '#match-table'
                expect(page).to have_css('.match-row', count: 2)

                expect(page.find('tr.match-row td', :text => 'Addy')).to be_truthy
                expect(page.find('tr.match-row td', :text => '6095551212')).to be_truthy
                expect(page.find('tr.match-row td', :text => 'Downstairs')).to be_truthy
            end

            it 'provides a way to cancel a match' do
                visit '/matches'

                page.find('.match-row', :match => :first).click_button('Cancel')

                expect(page).to have_selector '#match-table'
                expect(page).to have_css('.match-row', count: 1)

            end

        end
    end
end