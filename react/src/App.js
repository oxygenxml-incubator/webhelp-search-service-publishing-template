import React from "react";
import SearchComponent from "./components/SearchComponent.jsx";
import SearchInformation from "./components/SearchInformation.jsx";
import HitsList from "./components/HitsList.jsx";
import HitsItem from "./components/hitsItem.jsx";

const App = () => {
  return (
    <div className="search-page">
      <div className="search-container">
        <SearchComponent />
      </div>
      <div className="results-container">
        <SearchInformation hitsInformation="0 documents found on query." pageInformation="Page 0/0" />
        <HitsList items={[<HitsItem title="Title" description="Something" />]} />
      </div>
    </div>
  );
};

export default App;
