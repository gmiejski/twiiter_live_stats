/**
 * Created by piotrek on 6/7/15.
 */
function formatDate(picker) {
    var date = getData(picker).date();
    return date == null ? null : date.format('YYYY-MM-DD-HH:MM');
}

function getData(picker) {
    return picker.data("DateTimePicker");
}

function draw(keywords, occurrences) {
    var width = 800,
        bar_height = 20,
        height = bar_height * keywords.length,
        left_width = 100,
        gap = 2;

    var x = d3.scale.linear()
        .domain([0, d3.max(occurrences)])
        .range([0, width]);

    var y = d3.scale.ordinal()
        .domain(occurrences)
        .rangeBands([0, (bar_height + 2 * gap) * keywords.length]);

    d3.select('#barchart').select('svg').remove();

    var chart = d3.select('#barchart')
        .append('svg')
        .attr('class', 'chart')
        .attr('width', left_width + width + 40)
        .attr('height', (bar_height + gap * 2) * keywords.length + 30)
        .append("g")
        .attr("transform", "translate(10, 20)");

    chart.selectAll("line")
        .data(x.ticks(d3.max(occurrences)))
        .enter().append("line")
        .attr("x1", function (d) {
            return x(d) + left_width;
        })
        .attr("x2", function (d) {
            return x(d) + left_width;
        })
        .attr("y1", 0)
        .attr("y2", (bar_height + gap * 2) * keywords.length);

    chart.selectAll(".rule")
        .data(x.ticks(d3.max(occurrences)))
        .enter().append("text")
        .attr("class", "rule")
        .attr("x", function (d) {
            return x(d) + left_width;
        })
        .attr("y", 0)
        .attr("dy", -6)
        .attr("text-anchor", "middle")
        .attr("font-size", 10)
        .text(String);

    chart.selectAll("rect")
        .data(occurrences)
        .enter().append("rect")
        .attr("x", left_width)
        .attr("y", function (d) {
            return y(d) + gap;
        })
        .attr("width", x)
        .attr("height", bar_height);

    chart.selectAll("text.score")
        .data(occurrences)
        .enter().append("text")
        .attr("x", function (d) {
            return x(d) + left_width;
        })
        .attr("y", function (d, i) {
            return y(d) + y.rangeBand() / 2;
        })
        .attr("dx", -5)
        .attr("dy", ".36em")
        .attr("text-anchor", "end")
        .attr('class', 'score')
        .text(String);

    chart.selectAll("text.name")
        .data(keywords)
        .enter().append("text")
        .attr("x", left_width / 2)
        .attr("y", function (d, i) {
            return y(occurrences[i]) + y.rangeBand() / 2;
        })
        .attr("dy", ".36em")
        .attr("text-anchor", "middle")
        .attr('class', 'name')
        .text(String);
}

$(function () {
    var startPicker = $('#datetimepicker-start');
    var endPicker = $('#datetimepicker-end');
    var options = {
        showClear: true
    };

    startPicker.datetimepicker(options);
    endPicker.datetimepicker(options);
    startPicker.on("dp.change", function (e) {
        if (e.date != null) {
            getData(endPicker).minDate(e.date);
        }
    });
    endPicker.on("dp.change", function (e) {
        if (e.date != null) {
            getData(startPicker).maxDate(e.date)
        }
    });

    $("#reload-button").click(function () {
        $.get(
            "http://localhost:8080/keywords",
            {
                startDate: formatDate(startPicker),
                endDate: formatDate(endPicker)
            },
            function (data) {
                console.log(data);
            }
        );
    });

    var keywords = [];
    var occurrences = [];

    var ws = new WebSocket('ws://localhost:8085/main');

    ws.onmessage = function (message) {
        console.log(message.data);
        var matches = JSON.parse(message.data);
        for (var i = 0; i < matches.length; i++) {
            var keywordIndex = keywords.indexOf(matches[i]);
            if (keywordIndex == -1) {
                keywords.push(matches[i]);
                occurrences.push(1);
            } else {
                occurrences[keywordIndex]++;
            }
        }
        draw(keywords, occurrences)
    };
});