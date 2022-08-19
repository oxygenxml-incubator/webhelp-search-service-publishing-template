import { createAutocomplete } from '@algolia/autocomplete-core';
import { getAlgoliaResults } from '@algolia/autocomplete-preset-algolia';

import React, { useState, useMemo, useEffect, useRef } from 'react';

import { Highlight } from 'react-instantsearch-hooks-web';

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
                        <button className="aa-SubmitButton" type="submit" title="Submit">
                            <svg width="20" height="20" viewBox="0 0 20 20">
                                <path
                                    d="M14.386 14.386l4.0877 4.0877-4.0877-4.0877c-2.9418 2.9419-7.7115 2.9419-10.6533 0-2.9419-2.9418-2.9419-7.7115 0-10.6533 2.9418-2.9419 7.7115-2.9419 10.6533 0 2.9419 2.9418 2.9419 7.7115 0 10.6533z"
                                    fill="none"
                                    fillRule="evenodd"
                                    stroke="currentColor"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="1.4"
                                />
                            </svg>
                        </button>
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
                    <button className="aa-ClearButton" title="Clear" type="reset">
                        <svg
                            width="20"
                            height="20"
                            viewBox="0 0 20 20"
                            fill="currentColor"
                        >
                            <path
                                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                fillRule="evenodd"
                                clipRule="evenodd"
                            />
                        </svg>
                    </button>
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
                                                            <div className="aa-ItemActions">
                                                                <button
                                                                    className="aa-ItemActionButton aa-DesktopOnly aa-ActiveOnly"
                                                                    type="button"
                                                                    title="Select"
                                                                    style={{ pointerEvents: 'none' }}
                                                                >
                                                                    <svg fill="currentColor" viewBox="0 0 24 24">
                                                                        <path d="M18.984 6.984h2.016v6h-15.188l3.609 3.609-1.406 1.406-6-6 6-6 1.406 1.406-3.609 3.609h13.172v-4.031z" />
                                                                    </svg>
                                                                </button>
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
