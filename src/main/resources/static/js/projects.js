(() => {
    const initializeProjectFilters = () => {
        const archive = document.querySelector("[data-project-archive]");

        if (!archive) {
            return;
        }

        const filterButtons = Array.from(
            archive.querySelectorAll("[data-project-filter]")
        );
        const projectCards = Array.from(
            archive.querySelectorAll("[data-project-card]")
        );
        const visibleCount = archive.querySelector("[data-visible-count]");
        const emptyState = archive.querySelector("[data-project-empty]");
        const resetButton = archive.querySelector("[data-project-reset]");

        if (
            filterButtons.length === 0
            || projectCards.length === 0
            || !visibleCount
            || !emptyState
        ) {
            return;
        }

        const applyFilter = (filter) => {
            let visibleProjects = 0;

            projectCards.forEach((card) => {
                const categories = (card.dataset.categories || "")
                    .split(/\s+/)
                    .filter(Boolean);
                const isVisible = filter === "ALL" || categories.includes(filter);

                card.hidden = !isVisible;
                visibleProjects += isVisible ? 1 : 0;
            });

            filterButtons.forEach((button) => {
                const isActive = button.dataset.projectFilter === filter;
                button.setAttribute("aria-pressed", String(isActive));
            });

            visibleCount.textContent = String(visibleProjects);
            emptyState.hidden = visibleProjects !== 0;
        };

        filterButtons.forEach((button) => {
            button.addEventListener("click", () => {
                applyFilter(button.dataset.projectFilter);
            });
        });

        if (resetButton) {
            resetButton.addEventListener("click", () => {
                applyFilter("ALL");
                filterButtons[0].focus();
            });
        }

        applyFilter("ALL");
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeProjectFilters, {
            once: true
        });
    } else {
        initializeProjectFilters();
    }
})();
