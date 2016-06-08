'use strict';

import FormUtils from "../../js/components/wizard/generator/FormUtils";
import Constants from "../../js/constants/Constants";

describe('FormUtils', () => {

    describe('isForm', () => {
        it('returns true for a form element.', () => {
            var form = {
                '@type': Constants.FORM.FORM,
                'hasQuestion': [
                    {}, {}
                ]
            };
            form[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.FORM];
            expect(FormUtils.isForm(form)).toBeTruthy();
        });

        it('returns false for non-form element.', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.QUESTION_SECTION];
            expect(FormUtils.isForm(question)).toBeFalsy();
        });
    });

    describe('isWizardStep', () => {
        it('returns true for a wizard step question', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.QUESTION_SECTION, Constants.FORM.LAYOUT.WIZARD_STEP];
            expect(FormUtils.isWizardStep(question)).toBeTruthy();
        });

        it('returns false for a section', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.QUESTION_SECTION];
            expect(FormUtils.isWizardStep(question)).toBeFalsy();
        });
    });

    describe('isSection', () => {
        it('returns true for a section.', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.QUESTION_SECTION];
            expect(FormUtils.isSection(question)).toBeTruthy();
        });

        it('returns false for a regular question.', () => {
            expect(FormUtils.isSection({})).toBeFalsy();
        });
    });

    describe('isTypeahead', () => {
        it('returns true for a typeahead question.', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.QUESTION_TYPEAHEAD];
            expect(FormUtils.isTypeahead(question)).toBeTruthy();
        });

        it('returns false for a regular question.', () => {
            expect(FormUtils.isTypeahead({})).toBeFalsy();
        });
    });

    describe('isDisabled', () => {
        it('returns true for a disabled question.', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.DISABLED];
            expect(FormUtils.isDisabled(question)).toBeTruthy();
        });

        it('returns false for enabled question.', () => {
            expect(FormUtils.isDisabled({})).toBeFalsy();
        });
    });

    describe('isHidden', () => {
        it('returns true for a hidden question.', () => {
            var question = {};
            question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.HIDDEN];
            expect(FormUtils.isHidden(question)).toBeTruthy();
        });

        it('returns false for a normal question', () => {
            expect(FormUtils.isHidden({})).toBeFalsy();
        });
    });
});
