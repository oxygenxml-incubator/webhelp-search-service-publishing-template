import React from 'react';

import { Highlight } from 'react-instantsearch-hooks-web';

const AutocompleteItem = ({ performSearch, item, source, autocomplete }) => {
    return (
        <li
            key={item.objectID}
            className="aa-Item"
            {...autocomplete.getItemProps({ item, source })}
            onClick={() => { performSearch(item.title, 0), autocomplete.setIsOpen(false) }}
        >
            <div className="aa-ItemWrapper">
                <div className="aa-ItemContent">
                    <div className="aa-ItemContentBody">
                        <div className="aa-ItemContentTitle">
                            <Highlight hit={item} attribute="title" />
                        </div>
                        <div className="aa-ItemContentDescription">
                            <Highlight hit={item} attribute="shortDescription" />
                        </div>
                    </div>
                </div>
            </div>
        </li>
    );
}

export default AutocompleteItem;