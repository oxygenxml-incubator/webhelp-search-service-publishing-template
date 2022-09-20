import React, { useEffect, useState } from 'react';
import SearchInformation from './SearchInformation.jsx';
import HitsList from './HitsList.jsx';
import FilterContainer from "../filter/FilterContainer.jsx";

import { searchableAttributes, facetFilters } from '../filter/FilterContainer.jsx';

/**
 * Function that loads an JS file into DOM and does something on load.
 * @param {*} url is the url to the JSON file.
 * @param {*} implementationCode is the function to perform on load of the script into the DOM.
 */
function loadJS(url, implementationCode) {
    // Url is URL of external file, implementationCode is the code
    // to be called from the file, location is the location to 
    // insert the <script> element
    var scriptTag = document.createElement('script');
    scriptTag.src = url;
    scriptTag.onload = implementationCode;
    scriptTag.onreadystatechange = implementationCode;
    document.body.appendChild(scriptTag);
};

/**
 * Class that renders a container with search results.
 * @param {*} result is the response from Algolia.
 * @param {*} navigateToPage is the function to perform a search in Algolia index.
 * @param {*} searchInstancelt is an initialized index of Algolia.
 * @returns a container with all the results from Algolia.
 */
const ResultsContainer = ({ result, navigateToPage, searchInstance }) => {
    /** Array that holds information about profiling facets. */
    const [profilingInformation, setProfilingInformation] = useState([]);
    /** An array of preset documentations in index to display them in filters section. */
    const [documentations, setDocumentations] = useState([]);

    /** Function that fetches available documentations from Algolia index. */
    async function fetchDocumentations() {
        let response = await searchInstance.search('', {
            facets: ['documentation']
        });

        setDocumentations(Object.keys(response.facets.documentation))
    }

    useEffect(async () => {
        // Fetch documentations after mounting the component.
        await fetchDocumentations();
        // Load JS with profiling information after mounting the component.
        loadJS('subject-scheme-values.json', () => { setProfilingInformation(subjectSchemeValues.subjectScheme.attrValues) });
    }, [])

    /** Check if the previous button should be disabled or not. */
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
                                id: "attribute-content",
                                description: "Content",
                                isFilter: false,
                                algoliaId: "content"
                            }
                        ]
                    },
                    documentations.length !== 0 ?
                        {
                            title: "Documentations",
                            options: documentations.map((key) => {
                                return {
                                    id: `documentation-${key}`,
                                    description: key,
                                    isFilter: true,
                                    algoliaId: `documentation:${key}`
                                }
                            })
                        } : null,
                    ...(documentations.length === 0 ? profilingInformation.map((profilingValue) => {
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
                    }) : [])
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