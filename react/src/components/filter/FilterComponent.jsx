import React from 'react';

let searchableAttributes = ["title", "shortDescription", "contents"];

const FilterComponent = ({ performSearch, query }) => {
    return (<form onChange={null}><div className="filter-container">
        {console.log(searchableAttributes)}
        <h4 className="filter-title">Find query in:</h4>
        <input className="filter-selection" type="checkbox" id="attribute-title" defaultChecked={searchableAttributes.includes('title') ? true : false} onChange={() => { searchableAttributes.includes('title') ? (searchableAttributes = searchableAttributes.filter((attribute) => { return attribute.localeCompare('title') }), performSearch(query, 0, searchableAttributes)) : (searchableAttributes.push('title'), performSearch(query, 0, searchableAttributes)) }}></input>
        <label className="filter-label" htmlFor="attribute-title">Title</label><br />
        <input className="filter-selection" type="checkbox" id="attribute-shortDescription" defaultChecked={searchableAttributes.includes('shortDescription') ? true : false} onChange={() => { searchableAttributes.includes('shortDescription') ? (searchableAttributes = searchableAttributes.filter((attribute) => { return attribute.localeCompare('shortDescription') }), performSearch(query, 0, searchableAttributes)) : (searchableAttributes.push('shortDescription'), performSearch(query, 0, searchableAttributes)) }}></input>
        <label className="filter-label" htmlFor="attribute-shortDescription">Short Description</label><br />
        <input className="filter-selection" type="checkbox" id="attribute-contents" defaultChecked={searchableAttributes.includes('contents') ? true : false} onChange={() => { searchableAttributes.includes('contents') ? (searchableAttributes = searchableAttributes.filter((attribute) => { return attribute.localeCompare('contents') }), performSearch(query, 0, searchableAttributes)) : (searchableAttributes.push('contents'), performSearch(query, 0, searchableAttributes)) }}></input>
        <label className="filter-label" htmlFor="attribute-contents">Contents</label><br />
    </div></form>);
}

export default FilterComponent;