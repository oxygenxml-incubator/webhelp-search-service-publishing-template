import { autocomplete, getAlgoliaResults } from "@algolia/autocomplete-js";

import "@algolia/autocomplete-theme-classic";

// Check if disableWebHelpDefaultSearchEngine() method is present.
if (WebHelpAPI.disableWebHelpDefaultSearchEngine) {
  WebHelpAPI.disableWebHelpDefaultSearchEngine();
}

// Connect to Algolia App with Search-only API key.
const algoliasearch = require("algoliasearch");
const searchClient = algoliasearch(
  "40V95VH5YU",
  "8e4e1e3ae2fc1931b0a5f5d3c8f7544d"
);

const indexName = "webhelp-search-service-publishing-template";

// Create a object that implements performSearchOperation() and onPageChangedHandler() methods so it can be used by WebHelp.
const algoliaSearch = {
  // Method that is called when Submit is performed.
  performSearchOperation(query, successHandler, errorHandler) {
    // Search for hits for the given query.
    let result;
    if (query.includes("label_")) {
      let tag = query.split("_");
      tag.shift();
      let facetFilters = `_tags:${tag.join(" ")}`;

      console.log(facetFilters);
      result = searchClient
        .initIndex(indexName)
        .search("", { facetFilters: [facetFilters] });
    } else {
      result = searchClient.initIndex(indexName).search(query);
    }

    result
      .then((obj) => {
        // Extract data from Promise and create a SearchMeta object with extracted data.
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
        console.log(result);

        // Extract data from Promise and create SearxhDocument object with extracted data.
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

        // Pass the extracted data to SearchResult so it can be displayed by WebHelp.
        successHandler(new WebHelpAPI.SearchResult(meta, documents));
      })
      .catch((error) => {
        console.log(error);
      });
  },

  // Actions to do when page of results is changed.
  onPageChangedHandler(pageToShow, query, successHandler, searchFailed) {
    // Get results on the next page using the given by user query.
    const result = searchClient.initIndex(indexName).search(query, {
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

// Check if setCustomSearchEngine() method is present in order to change it to Algolia engine.
if (WebHelpAPI.setCustomSearchEngine) {
  WebHelpAPI.setCustomSearchEngine(algoliaSearch);
}

const navigateToSearch = (state) => {
  const path =
    document.querySelector('meta[name="wh-path2root"]').content +
    "search.html?searchQuery=" +
    state.collections[0].items[state.activeItemId].title;

  window.location = path;
};

// If container with id autocomplete is present in the DOM then replace it with Algolia autocomplete.
if (document.getElementById("autocomplete")) {
  autocomplete({
    id: "webhelp-algolia-search",
    container: "#autocomplete",
    placeholder: "Search",

    initialState: {
      query: window.location.href.includes("=")
        ? decodeURI(
            window.location.href.substring(
              window.location.href.indexOf("=") + 1,
              window.location.href.length
            )
          )
        : "",
    },

    // Actions to perform when user submits the query.
    onSubmit(state) {
      // Check if it's not empty
      if (state.query != "") {
        if (state.activeItemId == null) {
          const path =
            document.querySelector('meta[name="wh-path2root"]').content +
            "search.html?searchQuery=" +
            state.state.query;

          window.location = path;
        } else {
          navigateToSearch(state);
        }
      }
    },

    // Actions to perform to get suggestions for user.
    getSources({ query }) {
      return [
        {
          sourceId: "topics",
          // Return URL of the selected item.
          getItemUrl({ item }) {
            return item.objectID;
          },
          // Get suggestions.
          getItems() {
            return getAlgoliaResults({
              searchClient,
              queries: [
                {
                  indexName: indexName,
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
          // HTML template that is used in order to display suggestions.
          templates: {
            item({ item, components, html, state }) {
              return html`<div
                class="aa-ItemWrapper"
                onclick="${() => {
                  navigateToSearch(state);
                }}"
              >
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
    // Navigator that handles user redirections when only keyboard(arrows and enter button) is used.
    navigator: {
      navigate({ state }) {
        navigateToSearch(state);
      },
    },
  });
}
