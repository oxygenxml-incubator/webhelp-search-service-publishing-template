import React from 'react';

const FilterComponent = ({ title, options, setData, isSetData, query }) => {
    return (
        <>
            <h4 className="filter-title">{title}</h4>
            {options.map((option) => {
                return (
                    <React.Fragment key={option.id}>
                        <input className="filter-selection" type="checkbox" defaultChecked={isSetData(option.algoliaId)} onClick={() => setData(option.algoliaId, option.isFilter, query)} id={option.id}></input>
                        <label className="filter-label" htmlFor={option.id}>{option.description}</label><br />
                    </React.Fragment>
                )
            })}
        </>
    );
}

export default FilterComponent;