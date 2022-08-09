import { autocomplete, getAlgoliaResults } from "@algolia/autocomplete-js";

import "@algolia/autocomplete-theme-classic";

if (WebHelpAPI.disableWebHelpDefaultSearchEngine) {
  WebHelpAPI.disableWebHelpDefaultSearchEngine();
}

const searchClient = algoliasearch(
  "40V95VH5YU",
  "8e4e1e3ae2fc1931b0a5f5d3c8f7544d"
);

const search = instantsearch({
  indexName: "webhelp-search-service-publishing-template",
  searchClient,
  routing: true,
});

const algoliaSearch = {
  performSearchOperation(query, successHandler, errorHandler) {
    const result = searchClient.initIndex(search.indexName).search(query);
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

if (WebHelpAPI.setCustomSearchEngine) {
  WebHelpAPI.setCustomSearchEngine(algoliaSearch);
}

if (document.getElementById("autocomplete")) {
  autocomplete({
    id: "webhelp-algolia-search",
    container: "#autocomplete",
    placeholder: "Search",

    onSubmit(params) {
      if (params.state.query != "") {
        const path =
          document.querySelector('meta[name="wh-path2root"]').content +
          "search.html?searchQuery=" +
          params.state.query;
        window.location = path;
      }
    },

    onStateChange(params) {
      console.log(params);
      if (params.state.activeItemId != null) {
        params.state.completion =
          params.state.collections[0].items[params.state.activeItemId].title;
        params.state.query =
          params.state.collections[0].items[params.state.activeItemId].title;
      }
    },

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
                    attributesToSnippet: ["title:10", "contents:30"],
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
                        attribute: "contents",
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
    navigator: {
      navigate({ state }) {
        state.completion = state.query;
        if (state.query != "") {
          const path =
            document.querySelector('meta[name="wh-path2root"]').content +
            "search.html?searchQuery=" +
            state.query;
          window.location = path;
        }
      },
    },
  });
}
