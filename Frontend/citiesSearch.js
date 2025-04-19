let cities = [];
// Load cities from cities.txt
function loadCities() {
    $.ajax({
        url: 'cities.txt',
        method: 'GET',
        dataType: 'text',
        success: function(data) {
            cities = data.split('\n')
                .map(city => city.trim())
                .filter(city => city.length > 0);
            $('.text-input').prop('disabled', false);
            console.log(`Loaded ${cities.length} cities`);
        },
        error: function(xhr, status, error) {
            console.error('Error loading cities:', error);
            $('.text-input').attr('placeholder', 'Error loading cities');
        }
    });
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// Optimized search function with limit
function searchCities(query, $inputField, maxResults = 5) {
    const q = query.toLowerCase();
    const results = [];
    let count = 0;

    // Binary search to find starting point
    let startIndex = binarySearchStart(q);
    
    // Collect matches
    for (let i = startIndex; i < cities.length && count < maxResults; i++) {
        const cityLower = cities[i].toLowerCase();
        if (cityLower.startsWith(q)) {
            results.push(cities[i]);
            count++;
        } else if (cityLower > q) {
            break;
        }
    }
    displaySuggestions(results, $inputField);
}

// Binary search for starting point
function binarySearchStart(query) {
    let left = 0;
    let right = cities.length - 1;
    
    while (left <= right) {
        const mid = Math.floor((left + right) / 2);
        const cityLower = cities[mid].toLowerCase();
        
        if (cityLower < query) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return left;
}

function displaySuggestions(filteredCities, $inputField) {
    const $suggestions = $('#suggestions');
    $suggestions.empty();
    
    if (filteredCities.length > 0 && $inputField.val().trim() !== '') {
        $.each(filteredCities, function(index, city) {
            $('<div>')
                .addClass('suggestion-item')
                .text(city)
                .on('click', function() {
                    $inputField.val(city);
                    $suggestions.hide();
                })
                .appendTo($suggestions);
        });
        const pos = $inputField.position();
        const suggestionsHeight = $suggestions.outerHeight();
        
        // Position the suggestions dropdown above the input field
        $suggestions.css({
            top: pos.top - suggestionsHeight, // Position above the input field
            left: pos.left,
            width: $inputField.outerWidth(),
            position: 'absolute',
            zIndex: 1000,
        }).show();
    } else {
        $suggestions.hide();
    }
}

// Event handlers with jQuery
$(document).ready(function() {
    const debouncedSearch = debounce(searchCities, 200);

    // Use event delegation to handle dynamically added inputs
    $(document).on('input', '.text-input', function() {
        debouncedSearch($(this).val(), $(this));
    });

    $(document).on('focus', '.text-input', function() {
        if ($(this).val().trim() !== '') {
            searchCities($(this).val(), $(this));
        }
    });

    $(document).on('click', function(e) {
        if (!$(e.target).closest('.text-input, #suggestions').length) {
            $('#suggestions').hide();
        }
    });

    // Start loading cities
    loadCities();
});