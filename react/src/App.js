import React, { useState } from "react";
import ResultsContainer from "./components/hits/ResultsContainer.jsx";

import algoliasearch from "algoliasearch/lite";
import loaderImage from "./img/loader.gif";

import AutocompleteComponent from "./components/autocomplete/AutocompleteComponent.jsx";

import FilterComponent from "./components/hits/FilterComponent.jsx";

// Create an Algolia SearchClient using App key and Search-only API key.
const searchClient = algoliasearch(
  "KLFWXPOEHY",
  "ff20cb14577be8b5eab7ead0857dd573"
);

// Create a Search Instance with needed index.
const searchInstance = searchClient.initIndex(
  "webhelp-search-service-publishing-template"
);

const App = () => {
  // Create preloader state
  const [isLoading, setLoading] = useState(false);
  const [isFound, setFound] = useState(false);

  // Create a state variable that stores the search result.
  const [result, setResult] = useState({
    hits: [],
    nbHits: 0,
    nbPages: 0,
    page: 0,
    query: "",
  });

  // Fetch the Algolia response based on written search term.
  const search = async (searchTerm, page) => {
    setFound(false);

    setLoading(true);

    // If search term is not empty then get the results.
    if (searchTerm.localeCompare("") !== 0) {
      let response = await searchInstance.search(searchTerm, {
        hitsPerPage: 10,
        page: page,
      });
      
      setResult(response);

      if (response.hits.length > 0) setFound(true);
    }

    setLoading(false);

    document.getElementsByClassName("aa-Input")[0].blur();
  };

  return (
    <>
      <AutocompleteComponent
        searchClient={searchClient}
        performSearch={search}
      />
      {isLoading ? (
        <div className="loader">
          <img src={loaderImage} />
        </div>
      ) : (
        <>
          <ResultsContainer result={result} navigateToPage={search} />
          {isFound && <FilterComponent />}
        </>
      )}
    </>
  );
};

export default App;
