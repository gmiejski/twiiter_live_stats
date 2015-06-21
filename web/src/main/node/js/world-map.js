/**
 * Created by piotrek on 6/21/15.
 */

$(function () {
    var countryCodes = {};
    var occurrencesByCountry = {};

    $.getJSON('data/country-codes.json', function (data) {
        countryCodes = data;
        refreshMap();
        $('#map-refresh-button').click(refreshMap);
    });

    function refreshMap() {
        $.get('http://localhost:8080/keywords', function (keywords) {
            occurrencesByCountry = getOccurrencesByCountry(keywords);
            reloadMap();
        });
    }

    function getOccurrencesByCountry(keywords) {
        var occurrencesByCountry = {};
        $.each(keywords, function (index, keyword) {
            var keywordValue = keyword.value;
            $.each(keyword.occurrencesByCountry, function (twoLetterCountryCode, count) {
                var countryCode = countryCodes[twoLetterCountryCode];
                if (countryCode in occurrencesByCountry) {
                    var occurrencesByKeyword = occurrencesByCountry[countryCode];
                    if (keywordValue in occurrencesByKeyword) {
                        occurrencesByKeyword[keywordValue] += count;
                    } else {
                        occurrencesByKeyword[keywordValue] = count;
                    }
                } else {
                    occurrencesByCountry[countryCode] = {};
                    occurrencesByCountry[countryCode][keywordValue] = count;
                }
            });
        });
        Object.keys(occurrencesByCountry).map(function (countryCode) {
            occurrencesByCountry[countryCode] = $.map(occurrencesByCountry[countryCode], function (count, keyword) {
                return {
                    keyword: keyword,
                    count: count
                }
            });
            occurrencesByCountry[countryCode].sort(function (a, b) {
                var x = a.count;
                var y = b.count;
                return ((x < y) ? 1 : ((x > y) ? -1 : 0));
            });
            occurrencesByCountry[countryCode] = occurrencesByCountry[countryCode].slice(0, 3);
        });
        return occurrencesByCountry;
    }

    function reloadMap() {
        d3.select('#world-map').select('svg').remove();

        $('#world-map').datamaps({
            data: createMapData(),
            projection: 'mercator',
            fills: {
                defaultFill: '#abdda4'
            },
            geographyConfig: {
                highlightBorderColor: '#bada55',
                popupTemplate: function (geography, data) {
                    return createPopupTemplate(data, geography);
                },
                highlightBorderWidth: 3
            }
        });

        function createMapData() {
            var maxOccurrencesSum = Math.max.apply(Math, $.map(occurrencesByCountry, function (occurrences) {
                return sumOccurrences(occurrences)
            }));

            return Object.keys(occurrencesByCountry).reduce(function (previous, current) {
                var occurrences = occurrencesByCountry[current];
                var occurrencesSum = sumOccurrences(occurrences);
                previous[current] = {
                    occurrences: occurrences,
                    fillColor: d3.interpolate('#ffffcc', '#800026')(Math.sqrt(occurrencesSum / maxOccurrencesSum))
                };
                return previous;
            }, {});
        }

        function sumOccurrences(occurrences) {
            return occurrences.map(function (occurrence) {
                return occurrence.count;
            }).reduce(function (a, b) {
                return a + b;
            }, 0);
        }

        function createPopupTemplate(data, geography) {
            var occurrencesString = '';
            if (data !== null) {
                var occurrenceStrings = $.map(data.occurrences, function (occurrence) {
                    return occurrence.keyword + ': ' + occurrence.count;
                });
                occurrencesString += '<div><div>' + occurrenceStrings.join('</div><div>') + '</div></div>';
            }
            return '<div class="hoverinfo"><div class="strong">' + geography.properties.name + '</div>' + occurrencesString + '</div>'
        }
    }
});