(() => {
    "use strict";

    const initCopyEmail = () => {
        const button = document.querySelector("[data-copy-email]");
        const feedback = document.querySelector("[data-copy-email-feedback]");

        if (!button || !feedback) {
            return;
        }

        const email = button.dataset.email;
        if (!email) {
            return;
        }

        let resetTimer = null;
        const resetCopyState = () => {
            button.textContent = "COPY_EMAIL";
            feedback.textContent = "";
            resetTimer = null;
        };

        button.addEventListener("click", async () => {
            if (!navigator.clipboard?.writeText) {
                feedback.textContent = "COPY_UNAVAILABLE_";
                return;
            }

            try {
                await navigator.clipboard.writeText(email);
                button.textContent = "COPIED_";
                feedback.textContent = "EMAIL_COPIED_";

                if (resetTimer !== null) {
                    window.clearTimeout(resetTimer);
                }
                resetTimer = window.setTimeout(resetCopyState, 2000);
            } catch (_error) {
                feedback.textContent = "COPY_UNAVAILABLE_";
            }
        });
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initCopyEmail, { once: true });
    } else {
        initCopyEmail();
    }
})();
