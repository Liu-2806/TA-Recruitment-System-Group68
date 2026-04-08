(function () {
  const sortSelect = document.getElementById("reviewQueueSort");
  const queueBody = document.getElementById("reviewQueueBody");

  if (!sortSelect || !queueBody) {
    return;
  }

  function compareRows(a, b, sortValue) {
    if (sortValue === "match-desc") {
      return Number(b.dataset.match) - Number(a.dataset.match);
    }

    if (sortValue === "course-asc") {
      return a.dataset.course.localeCompare(b.dataset.course);
    }

    if (sortValue === "name-asc") {
      return a.dataset.name.localeCompare(b.dataset.name);
    }

    return new Date(b.dataset.submitted) - new Date(a.dataset.submitted);
  }

  function sortQueue() {
    const rows = Array.from(queueBody.querySelectorAll("tr")).filter(function (tr) {
      return tr.dataset && tr.dataset.submitted;
    });
    const sortValue = sortSelect.value;

    rows.sort(function (a, b) {
      return compareRows(a, b, sortValue);
    });

    rows.forEach(function (row) {
      queueBody.appendChild(row);
    });
  }

  sortSelect.addEventListener("change", sortQueue);
  sortQueue();
})();
