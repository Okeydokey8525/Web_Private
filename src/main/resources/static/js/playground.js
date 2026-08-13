(() => {
    const initPixelBrush = () => {
        const canvas = document.querySelector("[data-pixel-brush-canvas]");
        const clearButton = document.querySelector("[data-pixel-brush-clear]");

        if (!(canvas instanceof HTMLCanvasElement) || !clearButton) {
            return;
        }

        const context = canvas.getContext("2d");

        if (!context) {
            return;
        }

        const brushSize = 12;
        let cssWidth = 0;
        let cssHeight = 0;
        let drawing = false;
        let activePointerId = null;
        let previousPoint = null;

        const clearCanvas = () => {
            context.clearRect(0, 0, cssWidth, cssHeight);
            drawing = false;
            activePointerId = null;
            previousPoint = null;
        };

        const resizeCanvas = () => {
            const nextWidth = Math.max(1, Math.round(canvas.clientWidth));
            const nextHeight = Math.max(1, Math.round(canvas.clientHeight));

            if (nextWidth === cssWidth && nextHeight === cssHeight) {
                return;
            }

            cssWidth = nextWidth;
            cssHeight = nextHeight;

            const dpr = Math.min(window.devicePixelRatio || 1, 2);
            canvas.width = Math.round(cssWidth * dpr);
            canvas.height = Math.round(cssHeight * dpr);
            context.setTransform(dpr, 0, 0, dpr, 0, 0);
            context.imageSmoothingEnabled = false;
            clearCanvas();
        };

        const getGridPoint = (event) => {
            const bounds = canvas.getBoundingClientRect();
            const x = Math.floor((event.clientX - bounds.left) / brushSize) * brushSize;
            const y = Math.floor((event.clientY - bounds.top) / brushSize) * brushSize;
            return { x, y };
        };

        const drawBlock = (point) => {
            context.fillStyle = "#35c76f";
            context.fillRect(point.x, point.y, brushSize, brushSize);
        };

        const drawTo = (point) => {
            if (!previousPoint) {
                drawBlock(point);
                previousPoint = point;
                return;
            }

            const xDistance = point.x - previousPoint.x;
            const yDistance = point.y - previousPoint.y;
            const steps = Math.max(
                1,
                Math.ceil(Math.max(Math.abs(xDistance), Math.abs(yDistance)) / brushSize)
            );

            for (let step = 1; step <= steps; step += 1) {
                drawBlock({
                    x: Math.round((previousPoint.x + (xDistance * step) / steps) / brushSize) * brushSize,
                    y: Math.round((previousPoint.y + (yDistance * step) / steps) / brushSize) * brushSize
                });
            }

            previousPoint = point;
        };

        const stopDrawing = (event) => {
            if (!drawing || (event.pointerId !== undefined && event.pointerId !== activePointerId)) {
                return;
            }

            drawing = false;
            previousPoint = null;

            if (activePointerId !== null && canvas.hasPointerCapture?.(activePointerId)) {
                canvas.releasePointerCapture(activePointerId);
            }

            activePointerId = null;
        };

        canvas.addEventListener("pointerdown", (event) => {
            if (drawing) {
                return;
            }

            event.preventDefault();
            drawing = true;
            activePointerId = event.pointerId;
            previousPoint = null;
            canvas.setPointerCapture?.(event.pointerId);
            drawTo(getGridPoint(event));
        });

        canvas.addEventListener("pointermove", (event) => {
            if (!drawing || event.pointerId !== activePointerId) {
                return;
            }

            event.preventDefault();
            drawTo(getGridPoint(event));
        });

        canvas.addEventListener("pointerup", stopDrawing);
        canvas.addEventListener("pointercancel", stopDrawing);
        canvas.addEventListener("lostpointercapture", stopDrawing);
        window.addEventListener("pointerup", stopDrawing);
        window.addEventListener("pointercancel", stopDrawing);
        clearButton.addEventListener("click", clearCanvas);

        if ("ResizeObserver" in window) {
            const resizeObserver = new ResizeObserver(resizeCanvas);
            resizeObserver.observe(canvas);
        } else {
            window.addEventListener("resize", resizeCanvas);
        }

        resizeCanvas();
    };

    const initDitherMachine = () => {
        const output = document.querySelector("[data-dither-output]");
        const densityInput = document.querySelector("[data-dither-density]");
        const scaleInput = document.querySelector("[data-dither-scale]");
        const densityOutput = document.querySelector("[data-dither-density-output]");
        const scaleOutput = document.querySelector("[data-dither-scale-output]");

        if (!output || !densityInput || !scaleInput || !densityOutput || !scaleOutput) {
            return;
        }

        const updatePattern = () => {
            const density = Number(densityInput.value);
            const scale = Number(scaleInput.value);
            output.style.setProperty("--dither-density", `${density * 2.5}%`);
            output.style.setProperty("--dither-scale", `${scale}px`);
            densityOutput.value = String(density);
            scaleOutput.value = String(scale);
        };

        densityInput.addEventListener("input", updatePattern);
        scaleInput.addEventListener("input", updatePattern);
        updatePattern();
    };

    const initializePlayground = () => {
        initPixelBrush();
        initDitherMachine();
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializePlayground, { once: true });
    } else {
        initializePlayground();
    }
})();
