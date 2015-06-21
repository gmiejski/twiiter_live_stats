/**
 * Created by piotrek on 6/7/15.
 */

$(function () {
    $('[data-toggle="tooltip"]').tooltip();

    var keywords = [];

    var startPicker = $('#datetimepicker-start');
    var endPicker = $('#datetimepicker-end');
    var options = {
        showClear: true
    };
    startPicker.datetimepicker(options);
    endPicker.datetimepicker(options);
    startPicker.on("dp.change", function (e) {
        getData(endPicker).minDate(getDateLimit(e.date));
    });
    endPicker.on("dp.change", function (e) {
        getData(startPicker).maxDate(getDateLimit(e.date));
    });

    $("#reload-button").click(function () {
        var startDate = formatDate(startPicker);
        var endDate = formatDate(endPicker);

        $.get(
            "http://localhost:8080/keywords",
            {
                startDate: startDate,
                endDate: endDate
            },
            function (data) {
                sortKeywords(data);
                keywords = data;
                redrawBarChart();
            }
        );

        if (endDate == null) {
            turnOnLiveUpdate();
        } else {
            turnOffLiveUpdate();
        }
    });

    $("#clear-button").click(function () {
        keywords = [];
        getData(startPicker).date(null);
        getData(endPicker).date(null);
        redrawBarChart();
        turnOnLiveUpdate();
    });

    var ws = new WebSocket('ws://localhost:8085/main');
    turnOnLiveUpdate();

    function getData(picker) {
        return picker.data("DateTimePicker");
    }

    function getDateLimit(date) {
        return date === null ? false : date;
    }

    function formatDate(picker) {
        var date = getData(picker).date();
        return date === null ? null : date.format('YYYY-MM-DD-HH:MM');
    }

    function sortKeywords(keywords) {
        keywords.sort(function (a, b) {
            var x = a.value;
            var y = b.value;
            return ((x < y) ? -1 : ((x > y) ? 1 : 0));
        });
    }

    function getKeyword(keyword) {
        return keyword.value;
    }

    function getFrequency(keyword) {
        return keyword.occurrences.length;
    }

    function turnOnLiveUpdate() {
        ws.onmessage = updateOccurrences;
        $('#live-update-icon').show();
    }

    function turnOffLiveUpdate() {
        ws.onmessage = doNothing;
        $('#live-update-icon').hide();
    }

    function updateOccurrences(message) {
        console.log(message.data);
        var newOccurrences = JSON.parse(message.data);
        $.each(newOccurrences, function (index, newOccurrence) {
            var existingKeywords = $.grep(keywords, function (kw) {
                return getKeyword(kw) === newOccurrence;
            });
            var existingKeywordsCount = existingKeywords.length;
            var currentTimestamp = Date.now();
            if (existingKeywordsCount == 1) {
                existingKeywords[0].occurrences.push(currentTimestamp);
            } else if (existingKeywordsCount == 0) {
                keywords.push({value: newOccurrence, occurrences: [currentTimestamp]});
                sortKeywords(keywords);
            } else {
                console.warn('WTF');
            }
        });
        redrawBarChart();
    }

    function doNothing(message) {
    }

    function redrawBarChart() {
        var margin = {top: 20, right: 20, bottom: 30, left: 40},
            width = 960 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;

        var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

        var y = d3.scale.linear()
            .range([height, 0]);

        var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

        var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .ticks(10)
            .tickFormat(d3.format("d"));

        d3.select('#barchart').select('svg').remove();

        var svg = d3.select("#barchart")
            .append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        x.domain(keywords.map(getKeyword));
        y.domain([0, d3.max(keywords, getFrequency)]);

        svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis);

        svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("Frequency");

        var bar = svg.selectAll(".bar")
            .data(keywords)
            .enter();

        bar.append("rect")
            .attr("class", "bar")
            .attr("x", function (kw) {
                return x(getKeyword(kw));
            })
            .attr("width", x.rangeBand())
            .attr("y", function (kw) {
                return y(getFrequency(kw));
            })
            .attr("height", function (kw) {
                return height - y(getFrequency(kw));
            });

        bar.append("text")
            .attr("class", "count")
            .attr("x", function (kw) {
                return x(getKeyword(kw)) + x.rangeBand() / 2;
            })
            .attr("y", function (kw) {
                return y(getFrequency(kw)) - 10;
            })
            .attr("dy", ".35em")
            .text(getFrequency);
    }
});