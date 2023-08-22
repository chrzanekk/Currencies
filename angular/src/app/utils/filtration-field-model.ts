import {FilterType} from "../enumeration/filter-type.model";


/**
 * @description Model for base-search-component used for filtration support.
 * @param id Filter name. Need to be unique. This field is a HTML object id and local storage unique object identifier.
 *           Parameter is used for FilterAboveHeader feature and need to point to IViewFilter.name value.
 * @param prefix Filter prefix (example: productIndexLike, date.greaterThan).
 * @param value Filter value.
 * @param type The type of search field. Used for creation correct input types.
 * @param fieldsList Use this field for naming positions inside dropdown list in select/multiselect lists.
 * @param translateDirective This field provides support for i18n translate directive.
 *                           You can use it for display translated filter name or leave it undefined if you are using @param displayName directly.
 * @param selectListTranslationPrefix Provides support for i18n translate directive for select/multiselect list. Leave it undefined if you want
 *                                    to use value of @param fieldsList directly.
 *                                    In modal window, this param will display core message for input field (input field is a trigger for open modal window action).
 * @param values Filter values from select/multiselect lists.
 * @param disabled Mark filter as readonly.
 * @param selectSeparator Optional string separator. Divide each @param fieldsList by separator and get 0 index of returned array as filter value for @param values.
 * @param dateForPicker Set this field if date inside input field with type date/date-time need to be predefined.
 * @param tooltipDirective This field provides support for display translated message in tooltip window. If the field is undefined or null the translateDirective
 *                          param will be used.
 * @param checked Boolean value of filter field. This value is used in CHECKBOX filter types and provides support for correct check/uncheck operations.
 * @param offlineFilterName (Not used in base-search) Offline filtration name. This is the field name (key) of object created from ../shared/model/ classes. For example: index, name or quantity.
 * @param offlineFilterBaseValue (Not used in base-search) Offline filter base value. This is the base value for @param offlineFilterName , for example: '' or 0 or false.
 * @param numberInQueue Parameter for positioning filter one after another.
 * @param displayName If @param translateDirective won't be provided this field will be displayed as filter name.
 *                    For display translated name check @param translateDirective .
 * @param showInColumnHeader parameter used in view generator for display filter in column header.
 * @param viewFilterId parameter represents id of persisted viewFilter entity. Mostly used for match data from MODAL_WINDOW filter.
 */
export interface IFiltrationField {
  id?: string;
  prefix?: string;
  value?: string;
  type?: FilterType;
  fieldsList?: any[];
  translateDirective?: string;
  selectListTranslationPrefix?: string;
  values?: string[];
  disabled?: boolean;
  selectSeparator?: string;
  dateForPicker?: string;
  tooltipDirective?: string;
  checked?: boolean;
  offlineFilterName?: string;
  offlineFilterBaseValue?: string;
  numberInQueue?: number;
  displayName?: string;
  showInColumnHeader?: boolean;
  viewFilterId?: number;
}

export class FiltrationField implements IFiltrationField {
  constructor(
    public id?: string,
    public prefix?: string,
    public value?: string,
    public type?: FilterType,
    public fieldsList?: any,
    public translateDirective?: string,
    public selectListTranslationPrefix?: string,
    public values?: string[],
    public disabled?: boolean,
    public selectSeparator?: string,
    public dateForPicker?: string,
    public tooltipDirective?: string,
    public checked?: boolean,
    public offlineFilterName?: string,
    public offlineFilterBaseValue?: string,
    public numberInQueue?: number,
    public displayName?: string,
    public showInColumnHeader?: boolean,
    public viewFilterId?: number
  ) {}
}
