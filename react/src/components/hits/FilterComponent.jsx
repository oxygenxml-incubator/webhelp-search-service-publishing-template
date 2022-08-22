import React from 'react';

const FilterComponent = () => {

    const allIndicesChecked = () => {
        let indicesCheckboxes = document.querySelectorAll('[id^="indices"]');

        for (let i = 1; i < indicesCheckboxes.length; i++) {
            if (!indicesCheckboxes[i].checked) {
                indicesCheckboxes[0].checked = false;
                return;
            }
        }

        indicesCheckboxes[0].checked = true;
    }

    const clickAllIndicesCheckbox = () => {
        let indicesCheckboxes = document.querySelectorAll('[id^="indices"]');

        for (let i = 1; i < indicesCheckboxes.length; i++) {
            indicesCheckboxes[i].checked = true;
        }
    }

    return (<form onChange={null}><div className="filter-container">
        <h4 className="filter-title">Indices</h4>
        <input className="filter-selection" type="checkbox" id="indices-all" onChange={clickAllIndicesCheckbox} defaultChecked={true}></input>
        <label className="filter-label" htmlFor="indices-all">All</label><br />
        <input className="filter-selection" type="checkbox" id="indices-project-documentation" onChange={allIndicesChecked} defaultChecked={true}></input>
        <label className="filter-label" htmlFor="indices-project-documentation">Project Documentation</label><br />
        <input className="filter-selection" type="checkbox" id="indices-mobile-sample" onChange={allIndicesChecked} defaultChecked={true}></input>
        <label className="filter-label" htmlFor="indices-mobile-sample">Mobile Sample</label><br />
    </div></form>);
}

export default FilterComponent;