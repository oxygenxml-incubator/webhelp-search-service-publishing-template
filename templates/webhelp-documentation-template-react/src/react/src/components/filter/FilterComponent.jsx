import React from 'react';

/**
 * Class that renders an filter component that holds checkboxes to activate certain filters.
 * @param {*} title is a String that represents section's title, for example: "Price:" 
 * @param {*} options is an Array of objects that holds Strings for its id(unique ID for React), algoliaId(name of the facet and value in Algolia), description(checkbox description, for example "under 200 dollars") and isFilter boolean.
 * @param {*} setData function to set searchableAttributes or facetFilters.
 * @param {*} isSetData functiont that verifies if a filters/attribute is selected.
 * @param {*} query is the current query.
 * @returns a filter component.
 */
const FilterComponent = ({ title, options, setData, isSetData, query }) => {
    return (
        <div className="filter-section">
            <h4 className="filter-title">{title}</h4>
            {options.map((option) => {
                return (
                    <React.Fragment key={option.id}>
                        <input className="filter-selection" type="checkbox" defaultChecked={isSetData(option.algoliaId)} onClick={() => setData(option.algoliaId, option.isFilter, query)} id={option.id}></input>
                        <label className="filter-label" htmlFor={option.id}>{option.description}</label><br />
                    </React.Fragment>
                )
            })}
        </div>
    );
}

export default FilterComponent;