import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { autocomplete, getAlgoliaResults } from "@algolia/autocomplete-js";
import algoliasearch from "algoliasearch/lite";

import "@algolia/autocomplete-theme-classic";
import "./App.css";

import algoliaConfig from "./../algolia-config.json";

// Check if disableWebHelpDefaultSearchEngine() method is present.
if (WebHelpAPI.disableWebHelpDefaultSearchEngine) {
  WebHelpAPI.disableWebHelpDefaultSearchEngine();
}

// Create an Algolia SearchClient using App key and Search-only API key.
const searchClient = algoliasearch(
  algoliaConfig.appId,
  algoliaConfig.searchOnlyKey
);

const indexName = algoliaConfig.indexName;

// Create a Search Instance with needed index.
const searchInstance = searchClient.initIndex(indexName);

const algoliaSearch = {
  // Method that is called when Submit is performed.
  performSearchOperation(query, successHandler, errorHandler) {
    const root = ReactDOM.createRoot(document.getElementById("search-results"));
    root.render(<App query={query} searchInstance={searchInstance} />);
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
      if (state.state.query.trim().length !== 0) {
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

      return;
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
                      ${item.shortDescription}
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
