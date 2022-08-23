import React from 'react';

const FilterComponent = ({ title, options }) => {
    return (<>
        <h4 className="filter-title">{title}</h4>
        {options.map((option) => {
            return (
                <React.Fragment key={option.id}>
                    <input className="filter-selection" type="checkbox" defaultChecked={true} id={option.id}></input>
                    <label className="filter-label" htmlFor={option.id}>{option.description}</label><br />
                </React.Fragment>)
        })}
    </>);
}

export default FilterComponent;