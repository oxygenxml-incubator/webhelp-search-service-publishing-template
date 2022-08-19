import React from 'react';

import AutoCompleteItem from "./AutocompleteItem.jsx";

const AutocompleteList = ({ performSearch, index, autocomplete, items, source }) => {
    return (<section key={`source-${index}`} className="aa-Source">
        {items.length > 0 && (
            <ul className="aa-List" {...autocomplete.getListProps()}>
                {items.map((item) => {
                    return (<AutoCompleteItem performSearch={performSearch} item={item} source={source} autocomplete={autocomplete} />)
                })}
            </ul>
        )}
    </section>);
}

export default AutocompleteList;