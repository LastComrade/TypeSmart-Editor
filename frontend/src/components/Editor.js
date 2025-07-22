import { useState, useCallback, useEffect } from 'react';
import { useEditor, EditorContent } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import axios from 'axios';
import debounce from 'lodash/debounce';
import classNames from 'classnames';
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

const Editor = () => {
    const [currentWord, setCurrentWord] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const [coords, setCoords] = useState({ top: 0, left: 0 });
    const [selectedIndex, setSelectedIndex] = useState(0);

    const editor = useEditor({
        extensions: [StarterKit],
        onUpdate: ({ editor }) => {
            const text = editor.getText();
            const words = text.split(/\s+/).filter(word => word.length > 0);
            const lastWord = words[words.length - 1] || '';
            const editorDOM = editor?.view?.dom;

            setCurrentWord(lastWord);
            console.debug('Current word detected:', lastWord);

            if (lastWord.length > 1) {
                const position = editor.state.selection.from;
                const coords = editor.view.coordsAtPos(position);
                const editorBounds = editorDOM.getBoundingClientRect();

                if (editorDOM && coords) {
                    const newCoords = {
                        top: coords.bottom - editorBounds.top + 80,
                        left: coords.left - editorBounds.left + 30,
                    };

                    setCoords(newCoords);
                    console.debug('Suggestion popup position calculated:', newCoords);

                    fetchSuggestions(lastWord);
                    setSelectedIndex(0);
                }
            } else {
                setShowSuggestions(false);
                console.debug('Suggestion box hidden - word too short');
            }
        }
    });

    const fetchSuggestions = useCallback(
        debounce(async (word) => {
            try {
                console.debug('Fetching suggestions for:', word);
                const response = await axios.get(`${API_BASE_URL}/suggestion-service/api/suggestions`, {
                    params: { word }
                });
                const result = response.data?.suggestions || [];
                console.debug('Fetched suggestions:', result);
                setSuggestions(result);
                setShowSuggestions(true);
            } catch (err) {
                console.error("Failed to fetch suggestions:", err);
                setSuggestions([]);
                setShowSuggestions(false);
            }
        }, 300),
        []
    );

    useEffect(() => {
        return () => {
            fetchSuggestions.cancel();
        };
    }, [fetchSuggestions]);

    return (
        <div className="p-4 max-w-3xl mx-auto mt-10 border rounded-xl shadow-md bg-white relative">
            <h2 className="text-2xl font-bold mb-4">TypeSmart Editor</h2>
            <div className="border rounded-lg p-3 bg-gray-50">
                <EditorContent editor={editor} />
            </div>

            {showSuggestions && suggestions.length > 0 && (
                <div
                    className="absolute bg-white border border-gray-300 rounded-md shadow-md px-3 py-2 z-10 w-64 max-h-48 overflow-y-auto"
                    style={{ top: coords.top, left: coords.left }}
                >
                    <p className="text-sm text-gray-500 mb-1">
                        Suggestions for: <strong>{currentWord}</strong>
                    </p>
                    <ul className="text-sm space-y-1">
                        {suggestions.map((s, i) => (
                            <li
                                key={i}
                                className={classNames(
                                    "cursor-pointer px-2 py-1 rounded transition-colors",
                                    {
                                        "bg-blue-500 text-white": i === selectedIndex,
                                        "hover:bg-blue-100": i !== selectedIndex
                                    }
                                )}
                                onMouseEnter={() => {
                                    console.debug('Hovered on suggestion:', s, 'Index:', i);
                                    setSelectedIndex(i);
                                }}
                                onClick={() => {
                                    console.debug('Selected suggestion:', s);
                                    const newText = editor
                                        .getText()
                                        .trim()
                                        .split(/\s+/)
                                        .slice(0, -1)
                                        .concat(s)
                                        .join(" ");
                                    editor.commands.setContent(newText);
                                    setShowSuggestions(false);
                                }}
                            >
                                {s}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default Editor;
