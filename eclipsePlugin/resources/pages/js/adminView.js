document.addEventListener("DOMContentLoaded", () => {
    init();
});

function init() {
    setLoading(true);
    setTimeout(() => {
        createGroups(getGroups());
        setLoading(false);
    }, 0);
}


function getGroups() {
    const data = jsToSwtCallback('ADMIN_GET_GROUP_LIST');
    return JSON.parse(data);
}
function createGroups(groupList) {
    const groupsDOM = document.getElementById('groupList');
    groupsDOM.innerHTML = ''; // clear before adding
    for (let i = 0; i < groupList.length; i++) {
        const group = groupList[i];

        const collapse = `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" onclick="deselectRest(1, 'groupList', 'observationList', 'observationProjectList');activate(this, 'groupList');createObservations(getObservations(${group.id}));">
        <div class="d-flex w-100 justify-content-between">
            <h5 class="mb-1">${group.id}: ${group.name}</h5>
            <small>Updated at: ${group.updatedAt || 'never'}</small>
        </div>
        <p class="mb-1">
            <i>${group.description}</i>
        </p>
        <small>Created at: ${group.createdAt || 'never'}</small>
    </a>`;
        groupsDOM.insertAdjacentHTML('beforeend', collapse);
    }
}
function activate(element, domId) {
    const dom = document.getElementById(domId);
    for (let i = 0; i < dom.children.length; i++) {
        const children = dom.children[i];
        children.classList.remove('active');
    }
    element.classList.add('active');
}

function getObservations(group) {
    const data = jsToSwtCallback('ADMIN_GET_GROUP_OBSERVATIONS', JSON.stringify({ group }));
    return JSON.parse(data);
}

function createObservations(observations) {
    const observationsDOM = document.getElementById('observationList');
    observationsDOM.innerHTML = ''; // clear before adding
    for (let i = 0; i < observations.length; i++) {
        const observation = observations[i];

        const collapse = `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" onclick="deselectRest(2, 'observationList', 'groupList', 'observationProjectList');activate(this, 'observationList');createObservationProjects(getObservationProjects('${observation.shortId}'));">
        <div class="d-flex w-100 justify-content-between">
            <h5 class="mb-1">${observation.id}: ${observation.shortId}</h5>
            <small>Updated at: ${observation.updatedAt || 'never'}</small>
        </div>
        <small>Created at: ${observation.createdAt || 'never'}</small>
    </a>`;
        observationsDOM.insertAdjacentHTML('beforeend', collapse);
    }
}


function getObservationProjects(shortId) {
    const data = jsToSwtCallback('ADMIN_GET_OBSERVATION_PROJECTS', JSON.stringify({ shortId }));
    return JSON.parse(data);
}

function createObservationProjects(observationProjects) {
    const observationProjectDOM = document.getElementById('observationProjectList');
    observationProjectDOM.innerHTML = ''; // clear before adding
    for (let i = 0; i < observationProjects.length; i++) {
        const observationProject = observationProjects[i];

        const collapse = `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" onclick="deselectRest(3, 'observationProjectList', 'groupList', 'observationList');activate(this, 'observationProjectList');downloadProject(${observationProject.id})">
        <div class="d-flex w-100 justify-content-between">
            <h5 class="mb-1">${observationProject.name}</h5>
            <small>Updated at: ${observationProject.updatedAt || 'never'}</small>
        </div>
        <!-- <p class="mb-1">
            Estudiantes de Programación II
        </p> -->
        <small>Created at: ${observationProject.createdAt || 'never'}</small>
    </a>`;
        observationProjectDOM.insertAdjacentHTML('beforeend', collapse);
    }
}
async function downloadProject(project) {
    const result = await showConfirmation({ title: 'Sure?', message: 'Do you want to download this project?', success: 'Continue', error: 'Go back' });
    if (result.isConfirmed) {
        setLoading(true);
        setTimeout(() => {
            jsToSwtCallback('ADMIN_DOWNLOAD_PROJECT', JSON.stringify({ project }));
            setLoading(false);
        }, 0);
    }
}

function deselectRest(mantain, ...ids) {
    for (let i = 0; i < ids.length; i++) {
        const id = ids[i];
        if (i > mantain) {
            document.getElementById(id).innerHTML = '';
        }
    }
}

function logout() {
    jsToSwtCallback('LOG_OUT');
}

async function showConfirmation({ title, message, success, error }) {
    return Swal.fire({
        title: title,
        text: message,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: success,
        cancelButtonText: error,
    });
}
function setLoading(loading) {
    document.getElementById('loader').style.display = loading ? 'block' : 'none';
}

function logout() {
    jsToSwtCallback('LOG_OUT');
}