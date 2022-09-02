import React from 'react';

import FilterComponent from './FilterComponent.jsx';

export let searchableAttributes = new Set([]);
export let facetFilters = new Set([]);

const FilterContainer = ({ sections, performSearch, query }) => {
    const clearAllFilters = (e) => {
        e.preventDefault();
        let checkboxes = document.querySelectorAll(['.filter-container input[type="checkbox"'])

        for (let i = 0; i < checkboxes.length; i++)
            checkboxes[i].checked = false;

        searchableAttributes.clear();
        facetFilters.clear();

        performSearch(query, 0);
    }

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

    const isSetData = (item) => { return (searchableAttributes.has(item) || facetFilters.has(item)) }

    return (
        <form onChange={null}>
            <div className="filter-container">
                <div className="filter-buttons">
                    <button className="filter-button" onClick={e => clearAllFilters(e)}>Clear all filters</button>
                </div>
                {sections.map((section) => { return (<FilterComponent key={section.title} title={section.title} options={section.options} setData={setData} isSetData={isSetData} query={query} />) })}
            </div>
        </form>
    );
}

export default FilterContainer;