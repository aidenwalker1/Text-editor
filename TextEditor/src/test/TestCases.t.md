# Test Suite
## Integration Tests ##
* 4 undo and applyUndoRedoButton Test
  * Get redo/undo buttons
    * Expected: text is default
  * Insert text
  * Undo
    * Expected: Text gone, button says redo insert
  * Redo
    * Expected: Text returned, button says undo insert
  * Undo
    * Expected: Text gone, button says redo insert
  * Insert text
    * Expected: New text, redo reset
  * Remove first char
  * Undo
    * Expected: Back to new text, redo says remove
  * Select text
  * Apply bold
    * Expected: Text is now bold
  * Undo
    * Expected: Text not bold, undo says insert
  * Undo
    * Expected: No text, undo is empty
* 20 load and changeTabTitle Test
  * Load txt file "PairWiseTitle.txt"
    * Expected: Tab title changed to "PairWiseTitle.txt"
    * Expected: "Load Complete. Title of tab also successfully changed." present in text box
* 21 StartFind and ShowFind Test
  * Enter Text "the the the" in text box
  * Call StartFind method
    * Expected: Indices matching occurrences of "the" properly filled
    * Expected: Indices properly had their background changed to CYAN
* 22 Apply Attributes Test
  * Enter Test into the text pane
    * Expected: Test is present in the text pane
  * Select "Test" and apply bold
    * Expected: "Test" appears with bold styling
  * Enter "abcd" into text pane after Test
    * Expected: "Testabcd" appears in text box with "Test" in bold and "abcd" in default style
## Neighborhood Tests ##
* 1 willApplyTypeface and applySuperScript Test
  * Insert text
    * Expected: No style
  * Select text
  * Apply Superscript
    * Expected: superscript applied
  * Reset superscript
    * Expected: superscript reset
  * Apply subscript
    * Expected: superscript not applied
  * Apply superscript
    * Expected: superscript applied, subscript not applied
  * Reset superscript
    * Expected: superscript not applied
  * Select first char
  * Apply superscript
    * Expected: superscript applied at first char
  * Select all chars
  * Apply superscript
    * Expected: superscript applied at all chars

* 2 willApplyTypeface and applySubscript Test
  * Insert text
    * Expected: No style
  * Select text
  * Apply subscript
    * Expected: subscript applied
  * Reset subscript
    * Expected: subscript reset
  * Apply superscript
    * Expected: subscript not applied
  * Apply subscript
    * Expected: subscript applied, superscript not applied
  * Reset subscript
    * Expected: subscript not applied
  * Select first char
  * Apply subscript
    * Expected: subscript applied at first char
  * Select all chars
  * Apply subscript
    * Expected: subscript applied at all chars
* 3 willApplyTypeface and applyNormalTypeface Test
  * Insert text
    * Expected: No style
  * Select text
  * Apply bold
    * Expected: bold applied
  * Apply italic
    * Expected: bold and italic applied
  * Reset bold
    * Expected: only italic applied
  * Reset italic
    * Expected: all styles reset
  * Select first char
  * Apply bold
    * Expected: only first char is bold
  * Select all chars
  * Apply bold
    * Expected: first char is still bold along with others
## System Tests ##
* 5 Undo and Redo
  * Enter text
  * Click undo
    * Expected: last char removed
  * Select text
  * Click bold
    * Expected: all text bold
  * Click undo
    * Expected: text not bold
  * Click redo
    * Expected: text bold
  * Click undo
  * Deselect text
  * Enter char
  * Redo
    * Expected: Can't redo
  * Undo
    * Expected: char is removed
  * Redo
    * Expected: char is back
* 6 Tabs
  * Get tabbed pane, text pane
    * Expected: Only have document 0, no text
  * Enter text
  * Make new tab
    * Expected: Have 2 tabs
  * Select new tab
    * Expected: New tab is empty
  * Enter text
  * Switch tabs
    * Expected: Old tab text is the same
  * Enter text
  * Add tab
    * Expected: Have 3 tabs, 1 empty
  * Switch tab
    * Expected: 3rd tab is empty
  * Remove tab
    * Expected: 2 tabs left, viewing 2nd tab
* 7 Alignment Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to alignment menu
  * Align center
    * Expected: text aligns to the center
  * Align right
    * Expected: text aligns to the right
  * Align left
    * Expected: text aligns to the left
* 8 Bold Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Select text
  * Click format menu then bold menu
    * Expected "123" styled with bold present
  * Undo bold
    * Expected "123" styled in default style
  * Bold carat location and insert "abc"
    * Expected: "123" in default style, "abc" in bold
* 9 Italic Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to italic button
  * Select text and click on format and italic buttons
    * Expected: "123" styled with italic
  * Undo italic
    * Expected: "123" without italics (default style)
  * Click italic button at carat location and enter "abc"
    * Expected: "abc" appears in italic, "123" appears in default style
* 10 underline test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to underline button
  * Select text and click on format and underline buttons
    * Expected: "123" styled with underline
  * Undo underline
    * Expected: "123" without underline (default style)
  * Click underline button at carat location and enter "abc"
    * Expected: "abc" appears in underline, "123" appears in default style
* 11 Strikethrough Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to strikethrough button
  * Select text and click on format and strikethrough buttons
    * Expected: "123" styled with strikethrough
  * Undo strikethrough
    * Expected: "123" without strikethrough (default style)
  * Click strikethrough button at carat location and enter "abc"
    * Expected: "abc" appears with strikethrough, "123" appears in default style
* 12 Subscript Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to subscript button
  * Select "23" and click on format and italic buttons
    * Expected: "23" styled with subscript
  * Undo subscript
    * Expected: "123" without subscript (default style)
  * Click subscript button at carat location and enter "abc"
    * Expected: "abc" appears in subscript, "123" appears in default style
* 13 Superscript Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to superscript button
  * Select "23" and click on format and italic buttons
    * Expected: "23" styled with superscript
  * Undo superscript
    * Expected: "123" without superscript (default style)
  * Click superscript button at carat location and enter "abc"
    * Expected: "abc" appears in superscript, "123" appears in default style
* 14 Clear formatting
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to bold, italic, and clear formatting buttons
  * Select text and click on format then bold and italics buttons
    * Expected: "123" styled with bold and italic
  * Click clear formatting button
    * Expected: "123" without bold or italics (default style)
* 15 Text Color Change Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to change text color button
  * Select text and click on format and change text color buttons
  * Select blue as the desired color and submit the color chooser
    * Expected: "123" has blue as its text color
* 16 Highlight Color Change Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to highlight button
  * Select "123" and click on format and highlight buttons
  * Select cyan as the desired color and submit the color chooser
    * Expected: "123" has cyan as its text color
* 17 Text Size Change Test
  * Enter text into text box
    * Expected: "123" present in text box
  * Get format menu to change text size button
  * Select text and click on format and change text size buttons
  * Select 8 as the desired size and submit
    * Expected: "123" has a size of 8
* 18 Save and Load Test
  * Enter Text "This will save and load successfully"
  * Click File -> Save
    * Expected: Tab name changed to "test.txt"
  * Click File -> New
    * Expected: Tab name changed to "Document 0"
  * Click File -> load
    * Expected: Tab name changed to "test.txt"
    * Expected: "This will save and load successfully" present in text box
* 19 Find and Replace Test
  * Enter Text "the the the the"
  * Click Edit -> Find
    * Enter Text "the" in dialogue
    * Click Ok button
  * Click Edit -> Replace
    * Enter Text "dog" in dialogue
    * Click Ok button
      * Expected: "dog dog dog dog" present in text box