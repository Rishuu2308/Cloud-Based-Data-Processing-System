### Video Exalanation link
https://drive.google.com/file/d/1-epRGEKQ-LVnqODUo1Fkg5-gfWUiaePP/view?usp=sharing
 
# Task Management API

A Django-based Task Management API that allows users to create tasks, assign them to users, and view tasks by user.

---

## Features

- Create tasks with name and description
- Assign tasks to one or more users
- Retrieve tasks assigned to a specific user

---

## Setup Instructions

1. **extract the ZIP**

2. **(Optional) Create a virtual environment:**

> This step is recommended to manage dependencies in an isolated environment, but it's okay if you're not using it. I did not use it.

```bash
python -m venv env
source env/bin/activate  # Windows: env\Scripts\activate
```

3. **Install dependencies:**

```bash
pip install -r requirements.txt
```

4. **Run migrations:**

```bash
python manage.py makemigrations
python manage.py migrate
```

5. **Create a superuser (for admin login):**

```bash
python manage.py createsuperuser
```

6. **Start the development server:**

```bash
python manage.py runserver
```


