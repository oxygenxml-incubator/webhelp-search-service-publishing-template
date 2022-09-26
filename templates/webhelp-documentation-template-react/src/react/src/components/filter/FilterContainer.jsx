import React from 'react';

import FilterComponent from './FilterComponent.jsx';

/** Collection that holds all the selected attributes. */
export let searchableAttributes = new Set([]);
/** Collection that holds all the facet filters. */
export let facetFilters = new Set([]);

/**
 * Class that renders an filter container that holds filter components to activate certain filters.
 * @param {*} sections is an Array of objects that holds a title for the Filter component and an Array of options.
 * @param {*} performSearch is the function used to perform search in Algolia index.
 * @param {*} query is the current query.
 * @returns a filter container with filter components.
 */
const FilterContainer = ({ sections, performSearch, query }) => {
    /** Function that clears all the checkboxes and collections when clicked. */
    const clearAllFilters = (e) => {
        e.preventDefault();
        // Select all the checkboxes in the page.
        let checkboxes = document.querySelectorAll(['.filter-container input[type="checkbox"'])

        // Uncheck all the checkboxes.
        for (let i = 0; i < checkboxes.length; i++)
            checkboxes[i].checked = false;

        // Clear all the collections.
        searchableAttributes.clear();
        facetFilters.clear();

        performSearch(query, 0);
    }

    /** Function that adds filters/attributes to collections. */
    const setData = (item, isFilter, query) => {
        if (isFilter) {
            if (facetFilters.has(item)) {
                facetFilters.delete(item)
                performSearch(query, 0, [...searchableAttributes], [...facetFilters])
            } else {
                facetFilters.add(item)
                performSearch(query, 0, [...searchableAttributes], [...facetFilters])
            };
        }
        else {
            if (searchableAttributes.has(item)) {
                searchableAttributes.delete(item)
                performSearch(query, 0, [...searchableAttributes], [...facetFilters])
            } else {
                searchableAttributes.add(item)
                performSearch(query, 0, [...searchableAttributes], [...facetFilters])
            };
        }
    }

    /** Function that verifies if an filter/attribute is added in collection. It is used in order to know which checboxes to check upon rendering component. */
    const isSetData = (item) => { return (searchableAttributes.has(item) || facetFilters.has(item)) }

    return (
        <form onChange={null}>
            <div className="filter-container">
                <div className="filter-buttons">
                    <span className="filter-text">Filters</span>
                    <button className="filter-button" onClick={e => clearAllFilters(e)}>Clear all</button>
                </div>
                {sections.map((section) => { if (section !== null) { return (<FilterComponent key={section.title} title={section.title} options={section.options} setData={setData} isSetData={isSetData} query={query} />) } })}
            </div>
        </form>
    );
}

export default FilterContainer;