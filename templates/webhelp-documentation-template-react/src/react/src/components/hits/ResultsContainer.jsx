import React, { useEffect, useState } from 'react';
import SearchInformation from './SearchInformation.jsx';
import HitsList from './HitsList.jsx';
import FilterContainer from "../filter/FilterContainer.jsx";
import jsonProfiling from "../../../../../../../doc/mobile-phone/out/webhelp-responsive/subject-scheme-values.json";

import { searchableAttributes, facetFilters } from '../filter/FilterContainer.jsx';

const ResultsContainer = ({ result, navigateToPage }) => {
    const profilingInformation = jsonProfiling.subjectScheme.attrValues;

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
        <div className="hits-and-manipulation">
            <FilterContainer performSearch={navigateToPage} query={result.query} sections={
                [
                    {
                        title: "Find query in",
                        options: [
                            {
                                id: "attribute-title",
                                description: "Title",
                                isFilter: false,
                                algoliaId: "title"
                            },
                            {
                                id: "attribute-shortDescription",
                                description: "Short Description",
                                isFilter: false,
                                algoliaId: "shortDescription"
                            },
                            {
                                id: "attribute-contents",
                                description: "Contents",
                                isFilter: false,
                                algoliaId: "contents"
                            }
                        ]
                    },
                    ...profilingInformation.map((profilingValue) => {
                        return {
                            title: profilingValue.name.charAt(0).toUpperCase() + profilingValue.name.slice(1),
                            options: profilingValue.values.map((option) => {
                                return {
                                    id: `attribute-${option.key}`,
                                    description: option.navTitle,
                                    isFilter: true,
                                    algoliaId: `${profilingValue.name}:${option.key}`,
                                }
                            })
                        }
                    })
                ]
            } />
            <HitsList hits={result.hits} />
        </div>
        {result.nbPages !== 0 &&
            (<div className="page-selection">
                <button
                    className={`${isPrevButtonDisabled() ? "page-selector page-selector-disabled" : "page-selector"}`}
                    onClick={() => navigateToPage(result.query, result.page - 1, [...searchableAttributes], [...facetFilters])} disabled={isPrevButtonDisabled() ? true : false}
                >
                    Previous
                </button>
                <button
                    className={`${isNextButtonDisabled() ? "page-selector page-selector-disabled" : "page-selector"}`}
                    onClick={() => navigateToPage(result.query, result.page + 1, [...searchableAttributes], [...facetFilters])} disabled={isNextButtonDisabled() ? true : false}
                >
                    Next
                </button>
            </div>)}
    </div>);

}

export default ResultsContainer;