import React, { useEffect, useState } from 'react';
import SearchInformation from './SearchInformation.jsx';
import HitsList from './HitsList.jsx';

const ResultsContainer = ({result, navigateToPage}) => {

    useEffect(() => {
        const [prevButton, nextButton] = document.getElementsByClassName('page-selector');

        if (result.page === 0) {
            if (result.nbPages > 1) {
                prevButton.className = 'page-selector-disabled';
                prevButton.setAttribute("disabled", "disabled");
            }
            else {
                prevButton.className = 'page-selector-disabled';
                nextButton.className = 'page-selector-disabled';

                prevButton.setAttribute("disabled", "disabled");
                nextButton.setAttribute("disabled", "disabled");
            }
        }
        else {
            if (result.page === result.nbPages - 1) {
                nextButton.className = 'page-selector-disabled';
                nextButton.setAttribute("disabled", "disabled");
            }
        }
    }, [])

    return (<div className="results-container">
        <SearchInformation
            nHits={result.nbHits}
            query={result.query}
            page={result.nbPages >= 1 ? result.page + 1 : result.page}
            pages={result.nbPages}
        />
        <HitsList hits={result.hits} />
        <div className="page-selection">
            <button
                className="page-selector"
                onClick={() => navigateToPage(result.query, result.page - 1)}
            >
                Previous
            </button>
            <button
                className="page-selector"
                onClick={() => navigateToPage(result.query, result.page + 1)}
            >
                Next
            </button>
        </div>
    </div>);

}

export default ResultsContainer;