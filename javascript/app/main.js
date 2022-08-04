import { autocomplete, getAlgoliaResults } from "@algolia/autocomplete-js";

import "@algolia/autocomplete-theme-classic";

const searchClient = algoliasearch(
  "40V95VH5YU",
  "8e4e1e3ae2fc1931b0a5f5d3c8f7544d"
);

const search = instantsearch({
  indexName: "webhelp-search-service-publishing-template",
  searchClient,
  routing: true,
});

autocomplete({
  onSubmit(params) {
    const path =
      document.querySelector('meta[name="wh-path2root"]').content +
      "search.html?searchQuery=" +
      params.state.query;
    window.location = path;
  },

  id: "webhelp-algolia-search",
  container: "#autocomplete",
  placeholder: "Search",
  getSources({ query }) {
    return [
      {
        sourceId: "topics",
        getItemUrl({ item }) {
          return item.objectID;
        },
        getItems() {
          return getAlgoliaResults({
            searchClient,
            queries: [
              {
                indexName: search.indexName,
                query,
                params: {
                  hitsPerPage: 5,
                  attributesToSnippet: ["title:10", "shortDescription:30"],
                  snippetEllipsisText: "…",
                },
              },
            ],
          });
        },
        templates: {
          item({ item, components, html }) {
            return html`<div class="aa-ItemWrapper">
              <div class="aa-ItemContent">
                <div class="aa-ItemContentBody">
                  <div class="aa-ItemContentTitle">
                    ${components.Highlight({
                      hit: item,
                      attribute: "title",
                    })}
                  </div>
                  <div class="aa-ItemContentDescription">
                    ${components.Snippet({
                      hit: item,
                      attribute: "shortDescription",
                    })}
                  </div>
                </div>
              </div>
            </div>`;
          },
        },
      },
    ];
  },
});

const algoliaSearch = {
  performSearchOperation(query, successHandler, errorHandler) {
    const result = searchClient.initIndex(search.indexName).search(query);
    console.log(result);
    result
      .then((obj) => {
        const meta = new WebHelpAPI.SearchMeta(
          "Algolia",
          obj.nbHits,
          obj.page,
          obj.hitsPerPage,
          obj.nbPages,
          query,
          false,
          false,
          false,
          false,
          false,
          false
        );

        const documents = obj.hits.map((it) => {
          return new WebHelpAPI.SearchDocument(
            it.objectID,
            it.title,
            it._snippetResult.contents.value,
            [],
            0,
            [],
            it._highlightResult.contents.matchedWords
          );
        });

        successHandler(new WebHelpAPI.SearchResult(meta, documents));
      })
      .catch((error) => {
        console.log(error);
      });
  },

  onPageChangedHandler(pageToShow, query, successHandler, searchFailed) {
    const result = searchClient.initIndex(search.indexName).search(query, {
      page: pageToShow,
    });

    result
      .then((obj) => {
        const meta = new WebHelpAPI.SearchMeta(
          "Algolia",
          obj.nbHits,
          obj.page,
          obj.hitsPerPage,
          obj.nbPages,
          query,
          false,
          false,
          false,
          false,
          false,
          false
        );

        const documents = obj.hits.map((it) => {
          return new WebHelpAPI.SearchDocument(
            it.objectID,
            it.title,
            it._snippetResult.contents.value,
            [],
            0,
            [],
            it._highlightResult.contents.matchedWords
          );
        });

        successHandler(new WebHelpAPI.SearchResult(meta, documents));
      })
      .catch((error) => {
        console.log(error);
      });
  },
};

WebHelpAPI.disableWebHelpDefaultSearchEngine();
WebHelpAPI.setCustomSearchEngine(algoliaSearch);
