import React from "react";
import SearchComponent from "./components/SearchComponent.jsx";
import SearchInformation from "./components/SearchInformation.jsx";
import HitsList from "./components/HitsList.jsx";
import HitsItem from "./components/hitsItem.jsx";
import data from "./../data.json";

const App = () => {
  return (
    <div className="search-page">
      <div className="search-container">
        <SearchComponent />
      </div>
      <div className="results-container">
        <SearchInformation
          nHits="0"
          query="query"
          page="0"
          pages="0"
        />
        <HitsList
          items={data.map((item) => {
            return (
              <HitsItem
                title={item.title}
                description={item.shortDescription}
                url={item.objectID}
                key={item.objectID}
              />
            );
          })}
        />
      </div>
    </div>
  );
};

export default App;
