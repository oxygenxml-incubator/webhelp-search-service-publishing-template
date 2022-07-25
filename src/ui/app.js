// Replace with your own values
const searchClient = algoliasearch(
	'KLFWXPOEHY',
	'ff20cb14577be8b5eab7ead0857dd573' // search only API key, not admin API key
)

const search = instantsearch({
	indexName: 'Syncro pages',
	searchClient,
	routing: true,
})

search.addWidgets([
	instantsearch.widgets.configure({
		hitsPerPage: 10,
	}),
])

search.addWidgets([
	instantsearch.widgets.searchBox({
		container: '#search-box',
		placeholder: 'Search for contacts',
	}),
])

search.addWidgets([
	instantsearch.widgets.hits({
		container: '#hits',
		templates: {
			item: document.getElementById('hit-template').innerHTML,
			empty: `We didn't find any results for the search <em>"{{query}}"</em>`,
		},
	}),
])

search.start()
