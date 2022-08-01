const searchClient = algoliasearch('40V95VH5YU', '8e4e1e3ae2fc1931b0a5f5d3c8f7544d');

const search = instantsearch({
	indexName: 'webhelp-search-service-publishing-template',
    searchClient,
	routing: true,
})

const algoliaSearch = {
    performSearchOperation(query, successHandler, errorHandler){
        const result = searchClient.initIndex(search.indexName).search(query);
        console.log(result);
        result.then(obj => {
            const meta = new WebHelpAPI.SearchMeta('Algolia', obj.nbHits, obj.page, obj.hitsPerPage, obj.nbPages, query, false, false, false, false, false, false);
            
            const documents = obj.hits.map(it => {
                return new WebHelpAPI.SearchDocument(it.objectID, it.title, it._snippetResult.contents.value, [], 0, [], (it._highlightResult.title.matchedWords).concat(it._highlightResult.shortDescription.matchedWords, it._highlightResult.contents.matchedWords));
            });

            successHandler(new WebHelpAPI.SearchResult(meta, documents));
        }).catch(error => {
            console.log(error);
        }) 
    },

    onPageChangedHandler(pageToShow, query, successHandler, searchFailed){
       const result = searchClient.initIndex(search.indexName).search(query, {
        page: pageToShow,
      });

      result.then(obj => {
        const meta = new WebHelpAPI.SearchMeta('Algolia', obj.nbHits, obj.page, obj.hitsPerPage, obj.nbPages, query, false, false, false, false, false, false);
        
        const documents = obj.hits.map(it => {
            return new WebHelpAPI.SearchDocument(it.objectID, it.title, it._snippetResult.contents.value, [], 0, [], (it._highlightResult.title.matchedWords).concat(it._highlightResult.shortDescription.matchedWords, it._highlightResult.contents.matchedWords));
        });

        successHandler(new WebHelpAPI.SearchResult(meta, documents));
    }).catch(error => {
        console.log(error);
    }) 
    }
}

WebHelpAPI.disableWebHelpDefaultSearchEngine();
WebHelpAPI.setCustomSearchEngine(algoliaSearch);
