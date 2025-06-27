let isLoggedIn = false;
let studentEmail = '';

async function registerStudent() {
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;

  const response = await fetch("http://localhost:8888/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email })
  });

  const result = await response.text();
  document.getElementById("registerStatus").innerHTML = result.includes("Registered")
    ? "<span class='success'>✔️ Registered. Password sent to email.</span>"
    : "<span class='error'>❌ Registration failed.</span>";
}

async function login() {
  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;

  const response = await fetch("http://localhost:8888/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  });

  if (response.ok) {
    isLoggedIn = true;
    studentEmail = email;
    document.getElementById("loginStatus").innerHTML = "<span class='success'>✅ Login successful</span>";
  } else {
    document.getElementById("loginStatus").innerHTML = "<span class='error'>❌ Login failed.</span>";
  }
}

async function loadCourses() {
  const dropdown = document.getElementById("courseDropdown");
  dropdown.innerHTML = "";

  const response = await fetch("http://localhost:8888/courses");
  const courses = await response.json();

  if (courses.length === 0) {
    const opt = document.createElement("option");
    opt.text = "No courses available";
    dropdown.add(opt);
  } else {
    courses.forEach(c => {
      const opt = document.createElement("option");
      opt.value = c.name;
      opt.text = `${c.name} - ${c.seatsAvailable} seats`;
      dropdown.add(opt);
    });
  }
}

async function enrollCourse() {
  const course = document.getElementById("courseDropdown").value;

  const response = await fetch("http://localhost:8888/enroll", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: studentEmail, courseName: course })
  });

  const msg = await response.text();
  document.getElementById("enrollStatus").innerHTML = response.ok
    ? `<span class='success'>🎉 ${msg}</span>`
    : `<span class='error'>❌ ${msg}</span>`;
}
