import React, { useState, useEffect } from "react";
import SearchComponent from "./components/SearchComponent.jsx";
import ResultsContainer from "./components/ResultsContainer.jsx";
import algoliasearch from "algoliasearch/lite";
import loaderImage from "./img/loader.gif";

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
    setLoading(true);

    // If search term is not empty then get the results.
    if (searchTerm.localeCompare("") !== 0) {
      let response = await searchInstance.search(searchTerm, {
        hitsPerPage: 10,
        page: page,
      });
      setResult(response);
    }

    setLoading(false);
  };

  return (
    <>
      <div className="search-container">
        <SearchComponent performSearch={search} />
      </div>
      {isLoading ? (
        <div className="loader">
          <img src={loaderImage} />
        </div>
      ) : (
        <ResultsContainer result={result} navigateToPage={search} />
      )}
    </>
  );
};

export default App;
