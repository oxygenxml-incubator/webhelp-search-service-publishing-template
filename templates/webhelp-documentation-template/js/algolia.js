// Connect and authenticate with your Algolia app
const searchClient = algoliasearch('KLFWXPOEHY', 'ff20cb14577be8b5eab7ead0857dd573');
const searchInstance = instantsearch({
	indexName: 'webhelp-search-service-publishing-template',
	searchClient,
	routing: true,
})

const algoliaSearch = {
    performSearchOperation(query, successHandler, errorHandler){
        const result = searchClient.initIndex(searchInstance.indexName).search(query);
        result.then(obj => {
            const meta = new WebHelpAPI.SearchMeta('Algolia', obj.nbHits, obj.page, obj.hitsPerPage, obj.nbPages, query, false, false, false, false, false, false);
            
            const documents = obj.hits.map(it => {
                return new WebHelpAPI.SearchDocument(it.objectID, it.title, "", [], 0, [], []);
            });

            successHandler(new WebHelpAPI.SearchResult(meta, documents));
        }).catch(error => {
            console.log(error);
        }) 
    },

    onPageChangedHandler(pageToShow, query, successHandler, searchFailed){
       const result = searchClient.initIndex(searchInstance.indexName).search(query, {
        page: pageToShow,
      });

      result.then(obj => {
        const meta = new WebHelpAPI.SearchMeta('Algolia', obj.nbHits, obj.page, obj.hitsPerPage, obj.nbPages, query, false, false, false, false, false, false);
        
        const documents = obj.hits.map(it => {
            return new WebHelpAPI.SearchDocument(it.objectID, it.title, "", [], 0, [], []);
        });

        successHandler(new WebHelpAPI.SearchResult(meta, documents));
    }).catch(error => {
        console.log(error);
    }) 
    }
}

WebHelpAPI.disableWebHelpDefaultSearchEngine();
WebHelpAPI.setCustomSearchEngine(algoliaSearch);