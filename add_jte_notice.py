import glob

NOTICE = '<!--\n  Copyright (c) 2025\u20132026 Maximilian Wei\u00dfb\u00f6ck\n  Licensed under the MIT License (see LICENSE file).\n-->\n'

files = glob.glob('/Users/max/git/NBHWebApp/NBH/src/main/jte/**/*.jte', recursive=True)
updated = 0
skipped = 0
for f in sorted(files):
    with open(f, 'r', encoding='utf-8') as fh:
        content = fh.read()
    if 'Copyright (c) 2025' not in content:
        with open(f, 'w', encoding='utf-8') as fh:
            fh.write(NOTICE + content)
        updated += 1
        print('Updated: ' + f)
    else:
        skipped += 1
        print('Skipped (already has notice): ' + f)

print('\nDone: ' + str(updated) + ' updated, ' + str(skipped) + ' skipped, ' + str(len(files)) + ' total.')