import React from 'react';

import FilterComponent from './FilterComponent.jsx';

const FilterContainer = ({sections}) => {
    return (<form onChange={null}>
        <div className="filter-container">
            {sections.map((section) => { return (<FilterComponent key={section.title} title={section.title} options={section.options} />) })}
        </div>
    </form>);
}

export default FilterContainer;