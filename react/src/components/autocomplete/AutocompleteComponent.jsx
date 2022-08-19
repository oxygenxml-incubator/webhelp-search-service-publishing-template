import { createAutocomplete } from '@algolia/autocomplete-core';
import { getAlgoliaResults } from '@algolia/autocomplete-preset-algolia';

import React, { useState, useMemo, useEffect, useRef } from 'react';

import { Highlight } from 'react-instantsearch-hooks-web';

import ClearButton from '.././buttons/ClearButton.jsx';
import SubmitButton from ".././buttons/SubmitButton.jsx"

import '@algolia/autocomplete-theme-classic';

const AutocompleteComponent = (props) => {
    const [autocompleteState, setAutocompleteState] = useState({
        collections: [],
        completion: null,
        context: {},
        isOpen: false,
        query: '',
        activeItemId: null,
        status: 'idle',
    });

    const autocomplete = useMemo(() => createAutocomplete({
        onSubmit({ state }) {
            if (state.query.trim().length !== 0)
                props.performSearch(state.query, 0)
        },

        onStateChange({ state }) {
            setAutocompleteState(state);

            if (state.activeItemId != null) {
                state.completion =
                    state.collections[0].items[state.activeItemId].title;
                state.query =
                    state.collections[0].items[state.activeItemId].title;
            }
        },

        getSources() {
            return [
                {
                    sourceId: 'documents',
                    getItems({ query }) {
                        return getAlgoliaResults({
                            searchClient: props.searchClient,
                            queries: [
                                {
                                    indexName: "webhelp-search-service-publishing-template",
                                    query,
                                    params: {
                                        hitsPerPage: 5,
                                        highlightPreTag: '<mark>',
                                        highlightPostTag: '</mark>'
                                    },
                                },
                            ],
                        });
                    },
                },
            ];
        }
    }, props), [props]);

    const inputRef = useRef(null);
    const formRef = useRef(null);
    const panelRef = useRef(null);
    const { getEnvironmentProps } = autocomplete;

    useEffect(() => {
        if (!formRef.current || !panelRef.current || !inputRef.current) {
            return undefined;
        }
        const { onTouchStart, onTouchMove } = getEnvironmentProps({
            formElement: formRef.current,
            inputElement: inputRef.current,
            panelElement: panelRef.current,
        });

        window.addEventListener('touchstart', onTouchStart);
        window.addEventListener('touchmove', onTouchMove);

        return () => {
            window.removeEventListener('touchstart', onTouchStart);
            window.removeEventListener('touchmove', onTouchMove);
        };

    }, [getEnvironmentProps, formRef, inputRef, panelRef]);

    return (
        <div className="aa-Autocomplete" {...autocomplete.getRootProps({})}>
            <form
                ref={formRef}
                className="aa-Form"
                {...autocomplete.getFormProps({ inputElement: inputRef.current })}
            >
                <div className="aa-InputWrapperPrefix">
                    <label className="aa-Label" {...autocomplete.getLabelProps({})}>
                        <SubmitButton />
                    </label>
                </div>
                <div className="aa-InputWrapper">
                    <input
                        className="aa-Input"
                        ref={inputRef}
                        {...autocomplete.getInputProps({ inputElement: inputRef.current })}
                    />
                </div>
                <div className="aa-InputWrapperSuffix">
                    <ClearButton />
                </div>
            </form>

            {autocompleteState.isOpen && (
                <div
                    ref={panelRef}
                    className={[
                        'aa-Panel',
                        'aa-Panel--desktop',
                        autocompleteState.status === 'stalled' && 'aa-Panel--stalled',
                    ]
                        .filter(Boolean)
                        .join(' ')}
                    {...autocomplete.getPanelProps({})}
                >
                    <div className="aa-PanelLayout aa-Panel--scrollable">
                        {autocompleteState.collections.map((collection, index) => {
                            const { source, items } = collection;

                            return (
                                <section key={`source-${index}`} className="aa-Source">
                                    {items.length > 0 && (
                                        <ul className="aa-List" {...autocomplete.getListProps()}>
                                            {items.map((item) => {
                                                return (
                                                    <li
                                                        key={item.objectID}
                                                        className="aa-Item"
                                                        {...autocomplete.getItemProps({ item, source })}
                                                    >
                                                        <div className="aa-ItemWrapper">
                                                            <div className="aa-ItemContent">
                                                                <div className="aa-ItemContentBody">
                                                                    <div className="aa-ItemContentTitle">
                                                                        <Highlight hit={item} attribute="title" />;
                                                                    </div>
                                                                    <div className="aa-ItemContentDescription">
                                                                        <Highlight hit={item} attribute="shortDescription" />;
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                );
                                            })}
                                        </ul>
                                    )}
                                </section>
                            );
                        })}
                    </div>
                </div>
            )}
        </div>
    );
}

export default AutocompleteComponent;
