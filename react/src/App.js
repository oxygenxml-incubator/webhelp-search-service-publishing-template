import React, { useState, useEffect } from "react";
import SearchComponent from "./components/SearchComponent.jsx";
import SearchInformation from "./components/SearchInformation.jsx";
import HitsList from "./components/HitsList.jsx";
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
  // Create a state variable that stores the search term.
  const [searchTerm, setSearchTerm] = useState("");

  // Fetch the Algolia response based on written search term.
  const search = async (searchTerm, page) => {
    setLoading(true);

    // If search term is not empty then get the results.
    if (searchTerm.localeCompare("") !== 0) {
      let response = await searchInstance.search(searchTerm, { page: page });
      setResult(response);
    }

    setLoading(false);
  };

  return (
    <>
      <div className="search-container">
        <SearchComponent
          onChange={(e) => {
            setSearchTerm(e.target.value.trim());
          }}
          onClick={(e) => {
            e.preventDefault(), search(searchTerm, 0);
          }}
        />
      </div>
      {isLoading ? (
        <div className="loader">
          <img src={loaderImage} />
        </div>
      ) : (
        <div className="results-container">
          <SearchInformation
            nHits={result.nbHits}
            query={result.query}
            page={result.nbPages >= 1 ? result.page + 1 : result.page}
            pages={result.nbPages}
          />
          <HitsList hits={result.hits} />
          <div className="page-selection">
            {result.page == 0 ? (
              result.nbPages > 1 && (
                <>
                  <button
                    className="page-selector-disabled"
                    onClick={() => search(searchTerm, result.page - 1)}
                    disabled
                  >
                    Previous
                  </button>
                  <button
                    className="page-selector"
                    onClick={() => search(searchTerm, result.page + 1)}
                  >
                    Next
                  </button>
                </>
              )
            ) : result.page === result.nbPages - 1 ? (
              <>
                <button
                  className="page-selector"
                  onClick={() => search(searchTerm, result.page - 1)}
                >
                  Previous
                </button>
                <button
                  className="page-selector-disabled"
                  onClick={() => search(searchTerm, result.page + 1)}
                  disabled
                >
                  Next
                </button>
              </>
            ) : (
              <>
                <button
                  className="page-selector"
                  onClick={() => search(searchTerm, result.page - 1)}
                >
                  Previous
                </button>
                <button
                  className="page-selector"
                  onClick={() => search(searchTerm, result.page + 1)}
                >
                  Next
                </button>
              </>
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default App;
