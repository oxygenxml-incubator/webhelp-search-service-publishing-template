import React from 'react';

export let searchableAttributes = new Set([]);
export let facetFilters = new Set([]);

const FilterComponent = ({ performSearch, query }) => {
    const clearAllFilters = (e) => {
        e.preventDefault();
        let checkboxes = document.querySelectorAll(['.filter-container input[type="checkbox"'])

        for (let i = 0; i < checkboxes.length; i++)
            checkboxes[i].checked = false;

        searchableAttributes.clear();
        facetFilters.clear();

        performSearch(query, 0);
    }

    return (<form onChange={null}><div className="filter-container">
        <div className="filter-buttons">
            <button className="filter-clear" onClick={e => clearAllFilters(e)}>Clear all filters</button>
        </div>
        <div className="attribute-selection">
            <h4 className="filter-title">Find query in:</h4>
            <input className="filter-selection" type="checkbox" id="attribute-title" defaultChecked={searchableAttributes.has('title') ? true : false} onChange={() => { searchableAttributes.has('title') ? (searchableAttributes.delete('title')) : (searchableAttributes.add('title')), performSearch(query, 0, [...searchableAttributes], [...facetFilters]) }}></input>
            <label className="filter-label" htmlFor="attribute-title">Title</label><br />
            <input className="filter-selection" type="checkbox" id="attribute-shortDescription" defaultChecked={searchableAttributes.has('shortDescription') ? true : false} onChange={() => { searchableAttributes.has('shortDescription') ? (searchableAttributes.delete('shortDescription')) : (searchableAttributes.add('shortDescription')), performSearch(query, 0, [...searchableAttributes], [...facetFilters]) }}></input>
            <label className="filter-label" htmlFor="attribute-shortDescription">Short Description</label><br />
            <input className="filter-selection" type="checkbox" id="attribute-contents" defaultChecked={searchableAttributes.has('contents') ? true : false} onChange={() => { searchableAttributes.has('contents') ? (searchableAttributes.delete('contents')) : (searchableAttributes.add('contents')), performSearch(query, 0, [...searchableAttributes], [...facetFilters]) }}></input>
            <label className="filter-label" htmlFor="attribute-contents">Contents</label><br />
        </div>
        <div className="product-selection">
            <h4 className="filter-title">Product:</h4>
            <input className="filter-selection" type="checkbox" id="facet-product" defaultChecked={facetFilters.has('product:X2000') ? true : false} onChange={() => { facetFilters.has('product:X2000') ? (facetFilters.delete('product:X2000')) : (facetFilters.add('product:X2000')), performSearch(query, 0, [...searchableAttributes], [...facetFilters]) }}></input>
            <label className="filter-label" htmlFor="facet-product">X2000</label><br />
        </div>
        <div className="audience-selection">
            <h4 className="filter-title">Audience:</h4>
            <input className="filter-selection" type="checkbox" id="facet-audience" defaultChecked={facetFilters.has('audience:Tehnician') ? true : false} onChange={() => { facetFilters.has('audience:Tehnician') ? (facetFilters.delete('audience:Tehnician')) : (facetFilters.add('audience:Tehnician')), performSearch(query, 0, [...searchableAttributes], [...facetFilters]) }}></input>
            <label className="filter-label" htmlFor="facet-audience">Tehnician</label><br />
        </div>
    </div></form>);
}

export default FilterComponent;