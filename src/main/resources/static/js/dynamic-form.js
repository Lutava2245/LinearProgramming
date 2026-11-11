function addField() {
    const coefficientCount = parseInt(document.getElementById('coefficient-count').value) || 0;
    const constraintCount = parseInt(document.getElementById('constraint-count').value) || 0;

    const containerObjective = document.getElementById('container-objective-function');
    const containerConstraints = document.getElementById('container-constraints');

    containerObjective.innerHTML = '';
    containerConstraints.innerHTML = '';

    const objWrapper = document.createElement('div');
    objWrapper.className = 'flex flex-wrap items-center justify-center gap-2 mt-3';

    let objHtml = `<span class="font-bold text-zinc-700 text-md mr-1">Z =</span>`;
    for (let i = 0; i < coefficientCount; i++) {
        objHtml += `
            <div class="flex items-center gap-1">
                <input type="number" step="any" class="w-12 p-1 border-b-2 border-zinc-300 focus:outline-none focus:border-zinc-800 text-md text-center" name="objectiveFunction[${i}]" placeholder="0" required />
                <span class="text-md font-medium text-zinc-700">x<sub>${i + 1}</sub></span>
                ${i < coefficientCount - 1 ? '<span class="text-zinc-400 font-bold mx-1">+</span>' : ''}
            </div>
        `;
    }
    objWrapper.innerHTML = objHtml;
    containerObjective.appendChild(objWrapper);

    for (let i = 0; i < constraintCount; i++) {
        const divConstraint = document.createElement('div');
        divConstraint.className = 'p-4 mb-3 bg-zinc-50 rounded-xl border border-zinc-200 shadow-sm';

        let htmlStructure = `
            <h4 class="text-xs font-bold uppercase tracking-wider text-zinc-500 mb-2">Restrição ${i + 1}</h4>
            <div class="flex flex-wrap items-center gap-2">
        `;

        for (let j = 0; j < coefficientCount; j++) {
            htmlStructure += `
                <div class="flex items-center gap-1">
                    <input type="number" step="any" class="w-12 max-w-auto p-1 border-b-2 border-zinc-300 bg-white focus:outline-none focus:border-zinc-800 text-md text-center" name="constraints[${i}].coefficients[${j}]" placeholder="0" required />
                    <span class="text-md font-medium text-zinc-700">x<sub>${j + 1}</sub></span>
                    ${j < coefficientCount - 1 ? '<span class="text-zinc-400 font-bold mx-1">+</span>' : ''}
                </div>
            `;
        }

        htmlStructure += `
                <select class="p-1 border-b-2 border-zinc-300 bg-white focus:outline-none focus:border-zinc-800 text-md font-semibold mx-1 text-center" name="constraints[${i}].constraintType" required>
                    <option value="LESS_EQUAL" selected>&#8804;</option>
                    <option value="GREATER_EQUAL">&#8805;</option>
                    <option value="EQUAL">=</option>
                </select>

                <input type="number" step="any" class="w-16 p-1 border-b-2 border-zinc-300 bg-white focus:outline-none focus:border-zinc-800 text-md font-semibold text-center" name="constraints[${i}].rightHandValue" placeholder="0" required />
            </div>
        `;

        divConstraint.innerHTML = htmlStructure;
        containerConstraints.appendChild(divConstraint);
    }
}