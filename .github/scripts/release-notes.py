import re
import sys
from pathlib import Path


def extract_notes(changelog, tag):
    version = re.escape(tag.removeprefix("v"))
    heading = re.compile(rf"^(?:\[v?{version}\]|v?{version})(?:\s|$)")
    sections = []
    active = None
    fence = None
    for line in changelog.splitlines(keepends=True):
        marker = re.match(r"^ {0,3}(`{3,}|~{3,})(.*)$", line)
        if fence is not None:
            if marker and marker[1].startswith(fence) and not marker[2].strip():
                fence = None
        elif marker:
            fence = marker[1]
        else:
            section = re.match(r"^ {0,3}#{1,2}[ \t]+(.+)", line)
            if section:
                active = None
                if heading.match(section[1]):
                    active = []
                    sections.append(active)
                continue
        if active is not None:
            active.append(line)

    notes = "".join(sections[0]).strip("\n") if len(sections) == 1 else ""
    if not notes.strip():
        raise ValueError(f"CHANGELOG.md must have one nonempty section for tag {tag}")
    return notes + "\n"


if __name__ == "__main__":
    tag = sys.argv[1]
    try:
        notes = extract_notes(Path("CHANGELOG.md").read_text(encoding="utf-8-sig"), tag)
    except ValueError as error:
        sys.exit(str(error))
    Path("RELEASE_NOTES.md").write_text(notes, encoding="utf-8", newline="\n")
