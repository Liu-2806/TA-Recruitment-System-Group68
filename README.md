# TA-Recruitment-System-Group68

## Group Members

| GitHub Username | QMID |
| --- | --- |
| `Liu-2806` | `231226532` |
| `ffelaine` | `231226347` |
| `deer-ice` | `231226406` |
| `ShaoyangZhu` | `231226370` |
| `skywalker11111` | `231226439` |
| `NoveAmberic` | `231226495` |

## 协作维护规则

从当前版本开始，每次代码修改后都必须同步更新以下文档：

- `README.md`
- `requirements.txt`
- `docs/代码结构树.md`
- 对应模块的中文说明手册

当前已补齐：

- 中文说明手册：`docs/简历输入模块中文说明手册.md`
- 代码结构树：`docs/代码结构树.md`
- 环境要求：`requirements.txt`

## Resume Input Demo

This repository includes a minimal Java demo for the resume-input pipeline under the EBU6304 coursework constraints:

- Java-based implementation
- Text-file storage only
- Modular components for later extension
- No database

### Demo Flow

- Read a PDF resume
- Extract resume text
- Convert it into structured candidate data
- Load job postings from a text file
- Generate a match score and explanation for each job
- Write outputs as text-based JSON files

### Main Files

- `src/main/java/edu/qmul/ta/` contains the Java source code
- `demo-data/job_postings.csv` contains sample jobs
- `demo-data/sample_resume.pdf` is the sample input resume
- `output/candidate-profile.json` is the structured candidate output
- `output/job-match-results.json` is the matching result output
- `docs/简历输入模块中文说明手册.md` is the Chinese manual for this module
- `docs/代码结构树.md` records the current code structure
- `requirements.txt` records the shared environment requirements
- `.env.example` is the template for private API configuration

### Run The Demo

```powershell
powershell -ExecutionPolicy Bypass -File scripts/run-demo.ps1
```

### Private API Configuration

Do not put API keys into the Java source code.

Recommended workflow:

1. Copy `.env.example` to `.env.local`
2. Fill in your own `LLM_API_URL`, `LLM_API_KEY`, and `LLM_MODEL`
3. Run `scripts/run-demo.ps1`

`scripts/run-demo.ps1` loads `.env.local` automatically, and `.env.local` is ignored by Git.

For DeepSeek, both of these forms are supported:

- `LLM_API_URL=https://api.deepseek.com`
- `LLM_API_URL=https://api.deepseek.com/v1`

The code will normalize them to the chat completions endpoint automatically.

When API mode is enabled, the terminal now prints:

- `Matcher mode: api-llm`
- `LLM endpoint: ...`
- `LLM model: ...`

If API mode is not enabled, it prints:

- `Matcher mode: local-keyword`

### API Matching Hook

The demo uses a local keyword matcher by default. It switches to an OpenAI-compatible API matcher when the following variables are available:

- `LLM_API_URL`
- `LLM_API_KEY`
- `LLM_MODEL`

When enabled, the matcher sends the structured resume data and one job posting at a time to the configured API and expects a response containing `score` and `explanation`.
