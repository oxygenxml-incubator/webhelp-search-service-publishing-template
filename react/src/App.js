import React, { useState, useEffect } from "react";
import SearchComponent from "./components/SearchComponent.jsx";
import SearchInformation from "./components/SearchInformation.jsx";
import HitsList from "./components/HitsList.jsx";
import HitsItem from "./components/hitsItem.jsx";
import algoliasearch from "algoliasearch/lite";

const searchClient = algoliasearch(
  "KLFWXPOEHY",
  "ff20cb14577be8b5eab7ead0857dd573"
);
const searchInstance = searchClient.initIndex(
  "webhelp-search-service-publishing-template"
);

const App = () => {
  const [result, setResult] = useState({ hits: [], nbHits: 0, nbPages: 0, page: 0, query: "" });
  const [searchTerm, setSearchTerm] = useState("");

  const search = async (searchTerm) => {
    const result = await searchInstance.search(searchTerm);

    setResult(result);
  };

  return (
    <>
      <div className="search-container">
        <SearchComponent
          onChange={(e) => {
            setSearchTerm(e.target.value);
          }}
          onClick={() => search(searchTerm)}
        />
      </div>
      <div className="results-container">
        <SearchInformation
          nHits={result.nbHits}
          query={result.query}
          page={result.page}
          pages={result.nbPages}
        />
        <HitsList
          items={ Object.keys(result.hits).length > 0 ? (result.hits.map((item) => {
            return (
              <HitsItem
                key={item.objectId}
                title={item.title}
                description={item.shortDescription}
                url={item.objectId}
              />
            );
          })) : []}
        />
      </div>
    </>
  );
};

export default App;
