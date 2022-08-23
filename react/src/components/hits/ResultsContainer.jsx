import React, { useState, useEffect } from 'react';
import SearchInformation from './SearchInformation.jsx';
import HitsList from './HitsList.jsx';
import FilterContainer from "../filter/FilterContainer.jsx";

const ResultsContainer = ({ result, navigateToPage }) => {
    const [isFound, setFound] = useState(false);

    const isPrevButtonDisabled = () => {
        return result.page === 0;
    }

    const isNextButtonDisabled = () => {
        return result.page === result.nbPages - 1;
    }

    useEffect(() => {
        setFound(false);

        if (result.hits.length > 0) setFound(true);
    }, [])

    return (<div className="results-container">
        <SearchInformation
            nHits={result.nbHits}
            query={result.query}
            page={result.nbPages >= 1 ? result.page + 1 : result.page}
            pages={result.nbPages}
        />
        <div className="hits-and-manipulation">
            {isFound && <FilterContainer sections={[{ title: "Find query in:", options: [{ description: "Title", id: "attribute-title" }, { description: "Keywords", id: "attribute-keywords" }, { description: "Short Description", id: "attribute-short-description" }, { description: "Contents", id: "attribute-contents" }] },
            { title: "Topics", options: [{ description: "First", id: "topics-first" }, { description: "Second", id: "topics-second" }, { description: "Third", id: "topics-third" }] }]} />}
            <HitsList hits={result.hits} />
        </div>
        {result.nbPages !== 0 &&
            (<div className="page-selection">
                <button
                    className={`${isPrevButtonDisabled() ? "page-selector page-selector-disabled" : "page-selector"}`}
                    onClick={() => navigateToPage(result.query, result.page - 1)} disabled={isPrevButtonDisabled() ? true : false}
                >
                    Previous
                </button>
                <button
                    className={`${isNextButtonDisabled() ? "page-selector page-selector-disabled" : "page-selector"}`}
                    onClick={() => navigateToPage(result.query, result.page + 1)} disabled={isNextButtonDisabled() ? true : false}
                >
                    Next
                </button>
            </div>)}
    </div>);

}

export default ResultsContainer;