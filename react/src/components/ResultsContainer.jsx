import React from 'react';
import SearchInformation from './SearchInformation.jsx';
import HitsList from './HitsList.jsx';

const ResultsContainer = ({ result, navigateToPage }) => {

    const isPrevButtonDisabled = () => {
        return result.page === 0;
    }

    const isNextButtonDisabled = () => {
        return result.page === result.nbPages - 1;
    }

    return (<div className="results-container">
        <SearchInformation
            nHits={result.nbHits}
            query={result.query}
            page={result.nbPages >= 1 ? result.page + 1 : result.page}
            pages={result.nbPages}
        />
        <HitsList hits={result.hits} />
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