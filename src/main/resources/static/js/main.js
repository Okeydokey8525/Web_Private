(() => {
    const initializeNavigation = () => {
        const trigger = document.querySelector("[data-nav-trigger]");
        const menu = document.querySelector("[data-mobile-nav]");
        const closeButton = document.querySelector("[data-nav-close]");

        if (!trigger || !menu || !closeButton) {
            return;
        }

        const desktopQuery = window.matchMedia("(min-width: 64rem)");
        const navigationLinks = menu.querySelectorAll("[data-nav-link]");
        const isOpen = () => !menu.hidden;
        const getFocusableElements = () => Array.from(
            menu.querySelectorAll("a[href], button:not([disabled])")
        );

        const openMenu = () => {
            menu.hidden = false;
            trigger.setAttribute("aria-expanded", "true");
            trigger.setAttribute("aria-label", "Close navigation menu");
            document.body.classList.add("is-nav-open");
            closeButton.focus();
        };

        const closeMenu = (restoreFocus = true) => {
            if (!isOpen()) {
                return;
            }

            menu.hidden = true;
            trigger.setAttribute("aria-expanded", "false");
            trigger.setAttribute("aria-label", "Open navigation menu");
            document.body.classList.remove("is-nav-open");

            if (restoreFocus) {
                trigger.focus();
            }
        };

        trigger.addEventListener("click", () => {
            if (isOpen()) {
                closeMenu();
            } else {
                openMenu();
            }
        });

        closeButton.addEventListener("click", () => closeMenu());

        navigationLinks.forEach((link) => {
            link.addEventListener("click", () => closeMenu());
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && isOpen()) {
                closeMenu();
                return;
            }

            if (event.key === "Tab" && isOpen()) {
                const focusableElements = getFocusableElements();
                const firstElement = focusableElements[0];
                const lastElement = focusableElements[focusableElements.length - 1];

                if (!firstElement || !lastElement) {
                    event.preventDefault();
                    return;
                }

                if (event.shiftKey && document.activeElement === firstElement) {
                    event.preventDefault();
                    lastElement.focus();
                } else if (!event.shiftKey && document.activeElement === lastElement) {
                    event.preventDefault();
                    firstElement.focus();
                }
            }
        });

        desktopQuery.addEventListener("change", (event) => {
            if (event.matches) {
                closeMenu(false);
            }
        });

        menu.hidden = true;
        trigger.setAttribute("aria-expanded", "false");
        trigger.setAttribute("aria-label", "Open navigation menu");
        document.body.classList.remove("is-nav-open");
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeNavigation, { once: true });
    } else {
        initializeNavigation();
    }
})();
