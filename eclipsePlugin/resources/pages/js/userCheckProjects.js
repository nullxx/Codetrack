document.addEventListener("DOMContentLoaded", () => {
  init();
});

function init() {
  setLoading(true);
  setTimeout(() => {
    createProjects(getProyects());
    setLoading(false);
  }, 0);
}


function getProyects() {
  const data = jsToSwtCallback('GET_PROJECT_LIST');
  return JSON.parse(data);
}
function createProjects(projectList) {
  const projectsDOM = document.getElementById('listProjects');
  projectsDOM.innerHTML = ''; // clear before adding
  for (let i = 0; i < projectList.length; i++) {
    const project = projectList[i];

    const collapse = `<div class="panel panel-default">
        <div class="panel-heading" style="flex-direction:row;">
          <h4 class="panel-title">
            <a
              data-toggle="collapse"
              data-parent="#listProjects"
              href="#collapse_project_${project.id}"
              >${project.name}</a>
          </h4>
          <div style="line-height: 15px; padding-top: 5px;">
          <span style="color:grey;">Created at: ${project.createdAt || 'never'}</span>
          <br />
          <span style="color:grey;">Updated at: ${project.updatedAt || 'never'}</span>
          <br />
          </div>
        </div>
        <div id="collapse_project_${project.id}" class="panel-collapse collapse in">
          <div class="panel-body">
            <input type="checkbox" id="project_${project.id}" onclick="changeProjectStatus(event)"${project.isAllowed ? ' checked' : ''}></input>
            <label for="project_${project.id}">${project.isAllowed ? '✅ SYNC ENABLED' : '❎ SYNC DISABLED'}: ${project.name}</label>
            <br />
            <button type="button" class="btn btn-primary btn-sm" onclick="syncProject(${project.id})"${!project.isAllowed ? ' disabled' : ''}>Sync full project</button>
            </div>
        </div>
      </div>`;
    projectsDOM.insertAdjacentHTML('beforeend', collapse);
  }
}

function changeProjectStatus(e) {
  const action = e.target.checked;
  const projectId = e.target.getAttribute('id').split('project_')[1];
  const data = {
    project: parseInt(projectId, 10),
    isAllowed: action,
  };
  e.target.removeAttribute('onchange'); // quit this listener because we are going to create other
  const projectsUpdated = jsToSwtCallback('SET_PROJECT_STATUS', JSON.stringify(data));
  init();
}

async function syncProject(projectId) {
  const result = await showConfirmation({ title: 'Sure?', message: 'Are you sure? This will sync the full project. It will take time depending of the size of the project', success: 'Continue', error: 'Go back' });
  if (result.isConfirmed) {
    const data = {
      project: parseInt(projectId, 10),
    };
    setLoading(true);
    setTimeout(() => {
      const success = jsToSwtCallback('SYNC_FULL_PROJECT', JSON.stringify(data));
      if (success) {
        init();
      }
      setLoading(false);
    }, 0);

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
